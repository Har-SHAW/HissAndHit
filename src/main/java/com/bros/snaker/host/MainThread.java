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
        this.metaData = (List<int[]>) ServerData.positions[ServerData.playerCount + 1];
    }

    private boolean checkIfDead(int[] next, int player) {
        if (next[0] < 0 || next[1] < 0 || next[0] >= Statics.ROW || next[1] >= Statics.COL) {
            return true;
        }
        for (int i = 0; i < ServerData.playerCount; i++) {
            if (i == player) continue;
            if (containsInHashSet(next, i)) {
                return true;
            }
        }
        return false;
    }

    private void addToHashSet(int[] pair, int player) {
        ServerData.hashSet.add(Converter.cantorPair(pair[0], pair[1], player));
    }

    private void removeFromHashSet(int[] pair, int player) {
        ServerData.hashSet.remove(Converter.cantorPair(pair[0], pair[1], player));
    }

    private boolean containsInHashSet(int[] pair, int player) {
        return ServerData.hashSet.contains(Converter.cantorPair(pair[0], pair[1], player));
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

            if (checkIfDead(next, i)) {
                metaData.get(i)[MetaIndexes.IS_DEAD] = 1;
                continue;
            }

            pos.addLast(next);
            addToHashSet(next, i);

            if (containsInHashSet(next, ServerData.playerCount)) {
                int[] pop = ServerData.positions[ServerData.playerCount].stream()
                        .filter(e -> e[0] == next[0] && e[1] == next[1]).findFirst().orElse(null);
                assert pop != null;
                removeFromHashSet(pop, ServerData.playerCount);
                ServerData.positions[ServerData.playerCount].remove(pop);

                int[] food = new int[]{rand.nextInt(Statics.ROW - 1), rand.nextInt(Statics.COL - 1)};
                ServerData.positions[ServerData.playerCount].addLast(food);
                addToHashSet(food, ServerData.playerCount);
            } else {
                int[] removed = pos.pollFirst();
                assert removed != null;
                removeFromHashSet(removed, i);
            }
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
