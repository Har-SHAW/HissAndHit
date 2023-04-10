package com.bros.HissAndHit.host;

import com.bros.HissAndHit.config.Statics;
import com.bros.HissAndHit.data.ServerData;
import com.bros.HissAndHit.utils.Converter;

import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class Server {
    public void init() {
        ServerData.positions = new Deque[ServerData.playerCount + 2];
        ServerData.directions = new int[ServerData.playerCount];
        ServerData.speed = new boolean[ServerData.playerCount];
        ServerData.positions[ServerData.playerCount + 1] = new LinkedList<>();
        ServerData.hashMap = new HashMap<>();
        ServerData.readyBarrier = new CyclicBarrier(ServerData.playerCount + 1);
        ServerData.loadBarrier = new CyclicBarrier(ServerData.playerCount + 1);
        ServerData.playerNames = new String[ServerData.playerCount];

        for (int i = 0; i < ServerData.playerCount; i++) {
            ServerData.positions[i] = new LinkedList<>();
            ServerData.positions[i].addLast(new int[]{20, 20 + i});
            ServerData.positions[i].addLast(new int[]{19, 20 + i});
            ServerData.positions[i].addLast(new int[]{18, 20 + i});
            ServerData.directions[i] = 0;
            ServerData.positions[ServerData.playerCount + 1].addLast(new int[]{6357019 + i * 10, 0, 0});
            ServerData.hashMap.put(Converter.cantorPair(20, 20 + i), i);
            ServerData.hashMap.put(Converter.cantorPair(19, 20 + i), i);
            ServerData.hashMap.put(Converter.cantorPair(18, 20 + i), i);
            ServerData.playerNames[i] = "Player " + (i + 1);
            ServerData.speed[i] = false;
        }

        ServerData.foodMap = new HashMap<>();
        Random rand = new Random();
        ServerData.positions[ServerData.playerCount] = new ArrayDeque<>();
        for (int i = 0; i < Statics.FOOD_SIZE; i++) {
            int[] food = new int[]{rand.nextInt(1, Statics.ROW) - 1, rand.nextInt(1, Statics.COL) - 1};
            ServerData.positions[ServerData.playerCount].addLast(food);
            ServerData.foodMap.put(Converter.cantorPair(food[0], food[1]), food);
        }
    }

    public void start() {
        Thread thread = new Thread(new ConnectionThread(ServerData.playerCount), "Connect Thread");
        thread.start();
    }
}
