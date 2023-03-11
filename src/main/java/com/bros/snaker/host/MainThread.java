package com.bros.snaker.host;

import com.bros.snaker.config.Directions;
import com.bros.snaker.config.Statics;
import javafx.util.Pair;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@RequiredArgsConstructor
public class MainThread implements Runnable {
    @NonNull
    int playerId;
    @NonNull
    Socket socket;
    @NonNull CyclicBarrier cyclicBarrier;

    public static String listToString(List<Deque<Pair<Integer, Integer>>> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Deque<Pair<Integer, Integer>> deque : list) {
            sb.append("[");
            for (Pair<Integer, Integer> pair : deque) {
                sb.append("(").append(pair.getKey()).append(",").append(pair.getValue()).append("),");
            }
            if (!deque.isEmpty()) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("],");
        }
        if (!list.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                updatePosition();
                cyclicBarrier.await();
                out.println(listToString(Server.positions));
            }
        } catch (InterruptedException | BrokenBarrierException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updatePosition() {
        Directions playerDirection = Server.directions.get(playerId - 1);
        Deque<Pair<Integer, Integer>> pos = Server.positions.get(playerId - 1);
        Pair<Integer, Integer> next = new Pair<>(
                pos.getLast().getKey() + Statics.directionValues[playerDirection.ordinal()][0],
                pos.getLast().getValue() + Statics.directionValues[playerDirection.ordinal()][1]
        );

        pos.addLast(next);
        pos.pollFirst();
    }
}
