package com.bros.HissAndHit.host;

import com.bros.HissAndHit.config.MetaIndexes;
import com.bros.HissAndHit.config.Statics;
import com.bros.HissAndHit.data.ServerData;
import com.bros.HissAndHit.utils.Converter;

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
        this.metaData = (List<int[]>) ServerData.positions[ServerData.playerCount + 1];
    }

    private void addToMap(int[] pair, int player) {
        ServerData.hashMap.put(Converter.cantorPair(pair[0], pair[1]), player + 1);
    }

    private void removeFromMap(int[] pair) {
        ServerData.hashMap.remove(Converter.cantorPair(pair[0], pair[1]));
    }

    private Integer containsInMap(int[] pair) {
        int pairValue = Converter.cantorPair(pair[0], pair[1]);
        return ServerData.hashMap.get(pairValue);
    }

    private void updatePosition() {
        for (int i = 0; i < ServerData.playerCount; i++) {

            if (metaData.get(i)[MetaIndexes.IS_DEAD] > 0) {
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

            Integer echo = containsInMap(next);
            if (echo == null) {
                int[] removed = pos.pollFirst();
                assert removed != null;
                removeFromMap(removed);
            } else if (echo == ServerData.playerCount + 1) {
                int[] pop = ServerData.positions[ServerData.playerCount].stream()
                        .filter(e -> e[0] == next[0] && e[1] == next[1]).findFirst().orElse(null);
                assert pop != null;
                removeFromMap(pop);
                ServerData.positions[ServerData.playerCount].remove(pop);

                int[] food = new int[]{rand.nextInt(Statics.ROW - 1), rand.nextInt(Statics.COL - 1)};
                ServerData.positions[ServerData.playerCount].addLast(food);
                addToMap(food, ServerData.playerCount);
            } else if (echo > 0 && echo != i + 1) {
                metaData.get(i)[MetaIndexes.IS_DEAD] = 1;
                continue;
            }

            pos.addLast(next);
            addToMap(next, i);
        }
    }

    @Override
    public void run() {
        ConcurrentLinkedQueue<PrintWriter> writers = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < ServerData.playerCount; i++) {
            try {
                writers.offer(new PrintWriter(sockets[i].getOutputStream(), true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(ServerData.playerCount);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(ServerData.playerCount);

        while (true) {
            try {
                updatePosition();
                String data = Converter.toString(ServerData.positions);
                Thread.sleep(100);
                cyclicBarrier.reset();
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
