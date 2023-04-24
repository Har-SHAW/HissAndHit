package com.bros.HissAndHit.host;

import com.bros.HissAndHit.data.ServerData;
import com.bros.HissAndHit.operations.Position;
import com.bros.HissAndHit.utils.Compressor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class MainThread implements Runnable {
    Socket[] sockets;
    Position position;
    ConcurrentLinkedQueue<PrintWriter> writers = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<OutputStream> streams = new ConcurrentLinkedQueue<>();
    CyclicBarrier cyclicBarrier = new CyclicBarrier(ServerData.playerCount);
    ExecutorService executor = Executors.newFixedThreadPool(ServerData.playerCount);

    MainThread(Socket[] sockets) {
        this.sockets = sockets;
        this.position = new Position();
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < ServerData.playerCount; i++) {
                writers.offer(new PrintWriter(sockets[i].getOutputStream(), true));
                streams.offer(sockets[i].getOutputStream());
            }

            ServerData.loadBarrier.await();
            sendAll(position.getNamesAndColor());

            Thread.sleep(100);

            byte[] bytes = Compressor.serialize(ServerData.positions);
            sendBuff(bytes);

            ServerData.readyBarrier.await();

            Timer timer = new Timer();
            Semaphore semaphore = new Semaphore(0);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (position.updatePosition()) {
                            byte[] bytes = Compressor.serialize(ServerData.positions);
                            cyclicBarrier.reset();
                            sendBuff(bytes);
                            return;
                        }

                        semaphore.release();
                        cancel();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, 0, 100);

            semaphore.acquire();

            for (int i = 0; i < ServerData.playerCount; i++) {
                sockets[i].close();
            }
        } catch (InterruptedException | BrokenBarrierException | IOException e) {
            e.printStackTrace();
        }
    }

    void sendAll(String data) {
        for (PrintWriter printWriter : writers) {
            executor.submit(() -> {
                printWriter.println(data);
            });
        }
    }

    void sendBuff(byte[] bytes) throws IOException {
        for (OutputStream stream : streams) {
            executor.submit(() -> {
                try {
                    cyclicBarrier.await();
                    stream.write(bytes);
                } catch (InterruptedException | BrokenBarrierException | IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
