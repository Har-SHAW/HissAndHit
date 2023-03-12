package com.bros.snaker.host;

import com.bros.snaker.config.Directions;
import com.bros.snaker.config.Statics;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MainThread implements Runnable {
    @NonNull
    int playerId;
    @NonNull
    Socket socket;
    @NonNull CyclicBarrier cyclicBarrier;
    Random rand = new Random();

    @Override
    public void run() {
        try {
            OutputStream outputStream = socket.getOutputStream();
            while (true) {
                updatePosition();

                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
                objectOutputStream.writeObject(Server.positions);
                byte[] byteArray = byteOutputStream.toByteArray();

                cyclicBarrier.await();

                outputStream.write(byteArray);
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
        if (Server.positions[Server.numberOfPlayers].stream().noneMatch(e -> e[0] == next[0] && e[1] == next[1])){
            pos.pollFirst();
        }else{
            int[] pop = Server.positions[Server.numberOfPlayers].stream().filter(e -> e[0] == next[0] && e[1] == next[1]).findFirst().get();
            Server.positions[Server.numberOfPlayers].remove(pop);
            Server.positions[Server.numberOfPlayers].addLast(new int[]{rand.nextInt(99), rand.nextInt(99)});
        }
    }
}
