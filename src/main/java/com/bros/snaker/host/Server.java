package com.bros.snaker.host;

import com.bros.snaker.config.Directions;
import com.bros.snaker.config.Statics;
import com.bros.snaker.data.ServerData;

import java.util.*;

public class Server {
    public void init() {
        ServerData.positions = new Deque[ServerData.numberOfPlayers + 1];
        ServerData.directions = new ArrayList<>();
        for (int i = 0; i < ServerData.numberOfPlayers; i++) {
            ServerData.positions[i] = new LinkedList<>();
            ServerData.positions[i].addLast(new int[]{20, 20});
            ServerData.directions.add(Directions.UP);
        }
        Random rand = new Random();
        ServerData.positions[ServerData.numberOfPlayers] = new LinkedList<>();
        for (int i = 0; i < Statics.FOOD_SIZE; i++) {
            ServerData.positions[ServerData.numberOfPlayers].addLast(new int[]{rand.nextInt(1, Statics.ROW) - 1, rand.nextInt(1, Statics.COL) - 1});
        }
    }

    public void start() {
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= ServerData.numberOfPlayers; i++) {
            threads.add(new Thread(new ControllerThread(i), "Controller Player-" + i));
        }
        for (int i = 0; i < ServerData.numberOfPlayers; i++) {
            threads.get(i).start();
        }

        Thread thread = new Thread(new ConnectionThread(ServerData.numberOfPlayers), "Connect Thread");
        thread.start();
    }
}
