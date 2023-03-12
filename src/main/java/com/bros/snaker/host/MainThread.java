package com.bros.snaker.host;

import com.bros.snaker.config.Directions;
import com.bros.snaker.config.Statics;
import javafx.util.Pair;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Deque;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@RequiredArgsConstructor
public class MainThread implements Runnable {
    @NonNull
    int playerId;
    @NonNull
    Socket socket;
    @NonNull CyclicBarrier cyclicBarrier;

    @Override
    public void run() {
        try {
            OutputStream outputStream = socket.getOutputStream();
            while (true) {
                updatePosition();
                cyclicBarrier.await();
                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
                objectOutputStream.writeObject(Server.positions);
                byte[] byteArray = byteOutputStream.toByteArray();
                outputStream.write(byteArray);
            }
        } catch (InterruptedException | BrokenBarrierException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updatePosition() {
        Directions playerDirection = Server.directions.get(playerId - 1);
        Deque<int[]> pos = Server.positions[playerId - 1];
        int[] next = {
                pos.getLast()[0] + Statics.directionValues[playerDirection.ordinal()][0],
                pos.getLast()[1] + Statics.directionValues[playerDirection.ordinal()][1]
        };

        pos.addLast(next);
        pos.pollFirst();
    }
}
