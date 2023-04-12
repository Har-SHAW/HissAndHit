package com.bros.HissAndHit.operations;

import com.bros.HissAndHit.config.Statics;
import com.bros.HissAndHit.data.ServerData;
import com.bros.HissAndHit.proto.Data;
import com.bros.HissAndHit.utils.Converter;

import java.util.Deque;
import java.util.Random;

public class Position {
    private final Random rand;
    private final boolean[] slowFlag;

    public Position() {
        this.rand = new Random();
        this.slowFlag = new boolean[ServerData.playerCount];
    }

    private void killPlayer(int id) {
        Data.MetaData meta = ServerData.metaData.getData(id);
        meta = meta.toBuilder()
                .setIsDead(true).build();
        ServerData.metaData = ServerData.metaData.toBuilder().setData(id, meta).build();

        while (!ServerData.positions[id].isEmpty()) {
            int[] pop = ServerData.positions[id].pop();
            removeFromPlayer(pop);
        }
    }

    private void addToPlayer(int[] pair, int player) {
        ServerData.hashMap.put(Converter.cantorPair(pair[0], pair[1]), player);
    }

    private void removeFromPlayer(int[] pair) {
        ServerData.hashMap.remove(Converter.cantorPair(pair[0], pair[1]));
    }

    private Integer containsInPlayer(int[] pair) {
        return ServerData.hashMap.get(Converter.cantorPair(pair[0], pair[1]));
    }

    private Data.Array1D containsInFood(int[] pair) {
        return ServerData.foodMap.get(Converter.cantorPair(pair[0], pair[1]));
    }

    private void addToFood(Data.Array1D food) {
        ServerData.foodMap.put(Converter.cantorPair(food), food);
    }

    private void removeFromFood(Data.Array1D pair) {
        ServerData.foodMap.remove(Converter.cantorPair(pair));
    }

    public boolean updatePosition() {
        int deadCount = 0;
        for (int i = 0; i < ServerData.playerCount; i++) {
            slowFlag[i] = !slowFlag[i];
            if (ServerData.speed[i] == -1 && slowFlag[i]) {
                continue;
            }

            if (ServerData.metaData.getData(i).getIsDead()) {
                deadCount++;
                continue;
            }

            Deque<int[]> pos = ServerData.positions[i];
            int[] last = pos.getLast();
            int[] comp = Statics.COMPS[ServerData.directions[i]];
            int[] next = {last[0] + comp[0], last[1] + comp[1]};

            if (next[0] < 0 || next[1] < 0 || next[0] >= Statics.ROW || next[1] >= Statics.COL) {
                killPlayer(i);
                continue;
            }

            Integer echo = containsInPlayer(next);
            if (echo != null && echo != i) {
                killPlayer(i);
                continue;
            }

            Data.Array1D foodVal = containsInFood(next);
            if (foodVal == null) {
                int[] removed = pos.pollFirst();
                assert removed != null;
                removeFromPlayer(removed);
            } else {
                removeFromFood(foodVal);
                Data.Array1D point = Data.Array1D.newBuilder()
                        .setX(rand.nextInt(Statics.ROW - 1))
                        .setY(rand.nextInt(Statics.COL - 1)).build();
                addToFood(point);

                Data.MetaData meta = ServerData.metaData.getData(i);
                meta = meta.toBuilder()
                        .setScore(meta.getScore() + 1).build();
                ServerData.metaData = ServerData.metaData.toBuilder().setData(i, meta).build();
            }

            pos.addLast(next);
            addToPlayer(next, i);

            if (ServerData.speed[i] == 1 && slowFlag[i]) {
                i--;
            }
        }

        return deadCount != ServerData.playerCount;
    }

    public String getNamesAndColor() {
        return String.join(";", ServerData.playerNames)
                + "|"
                + String.join(";", ServerData.playerColors);
    }
}
