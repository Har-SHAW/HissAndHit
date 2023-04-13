package com.bros.HissAndHit.host;

import com.bros.HissAndHit.config.Statics;
import com.bros.HissAndHit.data.ServerData;
import com.bros.HissAndHit.proto.Data;
import com.bros.HissAndHit.utils.Converter;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Server {
    public void init() {
        ServerData.positions = new Deque[ServerData.playerCount];
        ServerData.directions = new int[ServerData.playerCount];
        ServerData.speed = new int[ServerData.playerCount];

        ServerData.hashMap = new HashMap<>();
        ServerData.readyBarrier = new CyclicBarrier(ServerData.playerCount + 1);
        ServerData.loadBarrier = new CyclicBarrier(ServerData.playerCount + 1);
        ServerData.playerNames = new String[ServerData.playerCount];
        ServerData.playerColors = new String[ServerData.playerCount];

        Data.PlayersMetaData.Builder metaBuilder = Data.PlayersMetaData.newBuilder();

        for (int i = 0; i < ServerData.playerCount; i++) {
            ServerData.positions[i] = new LinkedList<>();
            ServerData.positions[i].addLast(new int[]{20, 20 + i * 10});
            ServerData.positions[i].addLast(new int[]{19, 20 + i * 10});
            ServerData.positions[i].addLast(new int[]{18, 20 + i * 10});
            ServerData.directions[i] = 0;
            metaBuilder.addData(Data.MetaData.newBuilder().setIsDead(false).setScore(0));
            ServerData.hashMap.put(Converter.cantorPair(20, 20 + i * 10), i);
            ServerData.hashMap.put(Converter.cantorPair(19, 20 + i * 10), i);
            ServerData.hashMap.put(Converter.cantorPair(18, 20 + i * 10), i);
            ServerData.playerNames[i] = "Player " + (i + 1);
            ServerData.speed[i] = 0;
        }

        ServerData.foodMap = new HashMap<>();
        Random rand = new Random();

        for (int i = 0; i < Statics.FOOD_SIZE; i++) {
            Data.Point point = Data.Point.newBuilder().setX(rand.nextInt(1, Statics.ROW) - 1).setY(rand.nextInt(1, Statics.COL) - 1).build();
            ServerData.foodMap.put(Converter.cantorPair(point), point);
        }

        ServerData.metaData = metaBuilder.build();
    }

    public void start() {
        Thread thread = new Thread(new ConnectionThread(ServerData.playerCount), "Connect Thread");
        thread.start();
    }
}
