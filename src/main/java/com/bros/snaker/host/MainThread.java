package com.bros.snaker.host;

import com.bros.snaker.config.MetaIndexes;
import com.bros.snaker.config.Statics;
import com.bros.snaker.data.ServerData;
import com.bros.snaker.utils.Converter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class MainThread implements Runnable {
    Socket[] sockets;
    Random rand;
    List<int[]> metaData;

    MainThread(Socket[] sockets) {
        this.sockets = sockets;
        this.rand = new Random();
        this.metaData = (List<int[]>) ServerData.positions[ServerData.numberOfPlayers + 1];
    }

    private void updatePosition() {
        for (int i = 0; i < ServerData.numberOfPlayers; i++) {

            if (metaData.get(i)[MetaIndexes.IS_DEAD] > 0){
                continue;
            }

            Deque<int[]> pos = ServerData.positions[i];
            int[] last = pos.getLast();
            int[] comp = Statics.COMPS[ServerData.directions[i]];
            int[] next = {last[0] + comp[0], last[1] + comp[1]};

            if (next[0] < 0 || next[1] < 0 || next[0] >= Statics.ROW || next[1] >= Statics.COL) {
                metaData.get(i)[MetaIndexes.IS_DEAD] = 1;
                continue;
            }

            pos.addLast(next);

            if (ServerData.positions[ServerData.numberOfPlayers].stream().noneMatch(e -> e[0] == next[0] && e[1] == next[1])) {
                pos.pollFirst();
            } else {
                int[] pop = ServerData.positions[ServerData.numberOfPlayers].stream().filter(e -> e[0] == next[0] && e[1] == next[1]).findFirst().orElse(null);
                ServerData.positions[ServerData.numberOfPlayers].remove(pop);
                ServerData.positions[ServerData.numberOfPlayers].addLast(new int[]{rand.nextInt(Statics.ROW - 1), rand.nextInt(Statics.COL - 1)});
                pos.addLast(new int[]{next[0] + comp[0], next[1] + comp[1]});
            }
        }
    }

    @Override
    public void run() {
        ConcurrentLinkedQueue<PrintWriter> writers = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < ServerData.numberOfPlayers; i++) {
            try {
                writers.offer(new PrintWriter(sockets[i].getOutputStream(), true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(ServerData.numberOfPlayers);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(ServerData.numberOfPlayers);

        while (true) {
            try {
                cyclicBarrier.reset();
                updatePosition();
                String data = Converter.toString(ServerData.positions);
                Thread.sleep(100);
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
