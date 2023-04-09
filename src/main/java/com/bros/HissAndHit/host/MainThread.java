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

    private void addToPlayer(int[] pair, int player) {
        ServerData.hashMap.put(Converter.cantorPair(pair[0], pair[1]), player);
    }

    private void removeFromPlayer(int[] pair) {
        ServerData.hashMap.remove(Converter.cantorPair(pair[0], pair[1]));
    }

    private Integer containsInPlayer(int[] pair) {
        return ServerData.hashMap.get(Converter.cantorPair(pair[0], pair[1]));
    }

    private int[] containsInFood(int[] pair) {
        return ServerData.foodMap.get(Converter.cantorPair(pair[0], pair[1]));
    }

    private void addToFood(int[] pair, int[] food) {
        ServerData.foodMap.put(Converter.cantorPair(pair[0], pair[1]), food);
    }

    private void removeFromFood(int[] pair) {
        ServerData.foodMap.remove(Converter.cantorPair(pair[0], pair[1]));
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

            Integer echo = containsInPlayer(next);
            int[] foodVal = containsInFood(next);

            if (echo == null && foodVal == null) {
                int[] removed = pos.pollFirst();
                assert removed != null;
                removeFromPlayer(removed);
            } else if (echo != null && echo != i) {
                metaData.get(i)[MetaIndexes.IS_DEAD] = 1;
                continue;
            }

            if (foodVal != null) {
                removeFromFood(foodVal);
                ServerData.positions[ServerData.playerCount].remove(foodVal);

                int[] food = new int[]{rand.nextInt(Statics.ROW - 1), rand.nextInt(Statics.COL - 1)};
                ServerData.positions[ServerData.playerCount].addLast(food);
                addToFood(food, food);
            }

            pos.addLast(next);
            addToPlayer(next, i);
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

        try {
            ServerData.readyBarrier.await();
            while (true) {
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
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
