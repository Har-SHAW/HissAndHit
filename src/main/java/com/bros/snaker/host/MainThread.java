package com.bros.snaker.host;

import com.bros.snaker.config.Directions;
import com.bros.snaker.config.Statics;
import com.bros.snaker.data.ServerData;
import com.bros.snaker.utils.Converter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Deque;
import java.util.Random;

public class MainThread implements Runnable {
    Socket[] sockets;
    Random rand;

    MainThread(Socket[] sockets) {
        this.sockets = sockets;
        this.rand = new Random();
    }

    private void updatePosition() {
        for (int i = 0; i < ServerData.numberOfPlayers; i++) {
            Directions directions = ServerData.directions.get(i);
            Deque<int[]> pos = ServerData.positions[i];
            int[] next = {
                    pos.getLast()[0] + Statics.COMPS[directions.ordinal()][0],
                    pos.getLast()[1] + Statics.COMPS[directions.ordinal()][1]
            };

            pos.addLast(next);

            if (ServerData.positions[ServerData.numberOfPlayers].stream().noneMatch(e -> e[0] == next[0] && e[1] == next[1])) {
                pos.pollFirst();
            } else {
                int[] pop = ServerData.positions[ServerData.numberOfPlayers].stream().filter(e -> e[0] == next[0] && e[1] == next[1]).toList().get(0);
                ServerData.positions[ServerData.numberOfPlayers].remove(pop);
                ServerData.positions[ServerData.numberOfPlayers].addLast(new int[]{rand.nextInt(1, Statics.ROW) - 1, rand.nextInt(1, Statics.COL) - 1});
                pos.addLast(new int[]{
                        next[0] + Statics.COMPS[directions.ordinal()][0],
                        next[1] + Statics.COMPS[directions.ordinal()][1]
                });
            }
        }
    }

    @Override
    public void run() {
        PrintWriter[] out = new PrintWriter[ServerData.numberOfPlayers];
        for (int i = 0; i < ServerData.numberOfPlayers; i++) {
            try {
                out[i] = new PrintWriter(sockets[i].getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        while (true) {
            updatePosition();
            String data = Converter.toString(ServerData.positions);
            try {
                Thread.sleep(150);
                for (PrintWriter printWriter : out) {
                    printWriter.println(data);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
