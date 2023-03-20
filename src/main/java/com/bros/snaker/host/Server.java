package com.bros.snaker.host;

import com.bros.snaker.config.Statics;
import com.bros.snaker.data.ServerData;
import com.bros.snaker.utils.Converter;

import java.util.*;

public class Server {
    public void init() {
        ServerData.positions = new Deque[ServerData.playerCount + 2];
        ServerData.directions = new int[ServerData.playerCount];
        ServerData.positions[ServerData.playerCount + 1] = new LinkedList<>();
        ServerData.hashSet = new HashSet<>();
        for (int i = 0; i < ServerData.playerCount; i++) {
            ServerData.positions[i] = new LinkedList<>();
            ServerData.positions[i].addLast(new int[]{20, 20 + i});
            ServerData.positions[i].addLast(new int[]{19, 20 + i});
            ServerData.positions[i].addLast(new int[]{18, 20 + i});
            ServerData.directions[i] = 0;
            ServerData.positions[ServerData.playerCount + 1].addLast(new int[]{6357019, 0});
            ServerData.hashSet.add(Converter.cantorPair(20, 20 + i, i));
            ServerData.hashSet.add(Converter.cantorPair(19, 20 + i, i));
            ServerData.hashSet.add(Converter.cantorPair(18, 20 + i, i));
        }
        Random rand = new Random();
        ServerData.positions[ServerData.playerCount] = new ArrayDeque<>();
        for (int i = 0; i < Statics.FOOD_SIZE; i++) {
            int[] food = new int[]{rand.nextInt(1, Statics.ROW) - 1, rand.nextInt(1, Statics.COL) - 1};
            ServerData.positions[ServerData.playerCount].addLast(food);
            ServerData.hashSet.add(Converter.cantorPair(food[0], food[1], ServerData.playerCount));
        }
    }

    public void start() {
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= ServerData.playerCount; i++) {
            threads.add(new Thread(new ControllerThread(i - 1), "Controller Player-" + i));
        }
        for (int i = 0; i < ServerData.playerCount; i++) {
            threads.get(i).start();
        }

        Thread thread = new Thread(new ConnectionThread(ServerData.playerCount), "Connect Thread");
        thread.start();
    }
}
