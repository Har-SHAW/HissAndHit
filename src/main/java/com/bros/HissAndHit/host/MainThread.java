package com.bros.HissAndHit.host;

import com.bros.HissAndHit.data.ServerData;
import com.bros.HissAndHit.operations.Position;
import com.bros.HissAndHit.utils.Converter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.*;

public class MainThread implements Runnable {
    Socket[] sockets;
    Position position;
    ConcurrentLinkedQueue<PrintWriter> writers = new ConcurrentLinkedQueue<>();
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
            }

            ServerData.loadBarrier.await();
            sendAll(position.getNamesAndColor());

            sendAll(Converter.toString(ServerData.positions));

            ServerData.readyBarrier.await();
            String data;
            while (position.updatePosition()) {
                data = Converter.toString(ServerData.positions);
                Thread.sleep(100);
                cyclicBarrier.reset();
                sendAll(data);
            }

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
                try {
                    cyclicBarrier.await();
                    printWriter.println(data);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
