package com.bros.snaker.host;

import com.bros.snaker.config.Directions;
import com.bros.snaker.config.Statics;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;

public class MainThread implements Runnable {
    int playerId;
    Socket socket;
    CyclicBarrier cyclicBarrier;
    Random rand = new Random();

    MainThread(int playerId, Socket socket, CyclicBarrier cyclicBarrier) {
        this.playerId = playerId;
        this.socket = socket;
        this.cyclicBarrier = cyclicBarrier;
    }

    public static String toString(Deque<int[]>[] dequeArray) {
        String separator = ";";
        return Arrays.stream(dequeArray)
                .map(deque -> Arrays.deepToString(deque.toArray(new int[0][])).replaceAll("\\s+", ""))
                .collect(Collectors.joining(separator));
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                updatePosition();
                String data = toString(Server.positions);
                cyclicBarrier.await();
                out.println(data);
            }
        } catch (InterruptedException | BrokenBarrierException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updatePosition() {
        Directions directions = Server.directions.get(playerId - 1);
        Deque<int[]> pos = Server.positions[playerId - 1];
        int[] next = {
                pos.getLast()[0] + Statics.COMPS[directions.ordinal()][0],
                pos.getLast()[1] + Statics.COMPS[directions.ordinal()][1]
        };

        pos.addLast(next);

        if (Server.positions[Server.numberOfPlayers].stream().noneMatch(e -> e[0] == next[0] && e[1] == next[1])) {
            pos.pollFirst();
        } else {
            int[] pop = Server.positions[Server.numberOfPlayers].stream().filter(e -> e[0] == next[0] && e[1] == next[1]).toList().get(0);
            Server.positions[Server.numberOfPlayers].remove(pop);
            Server.positions[Server.numberOfPlayers].addLast(new int[]{rand.nextInt(1, Statics.ROW) - 1, rand.nextInt(1, Statics.COL) - 1});
            pos.addLast(new int[]{
                    next[0] + Statics.COMPS[directions.ordinal()][0],
                    next[1] + Statics.COMPS[directions.ordinal()][1]
            });
        }
    }
}
