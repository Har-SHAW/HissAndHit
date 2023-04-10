package com.bros.HissAndHit.operations;

import com.bros.HissAndHit.config.MetaIndexes;
import com.bros.HissAndHit.config.Statics;
import com.bros.HissAndHit.data.ServerData;
import com.bros.HissAndHit.utils.Converter;

import java.util.Deque;
import java.util.List;
import java.util.Random;

public class Position {
    private final List<int[]> metaData;
    private final Random rand;
    private final boolean[] slowFlag;

    public Position() {
        this.rand = new Random();
        this.metaData = (List<int[]>) ServerData.positions[ServerData.playerCount + 1];
        this.slowFlag = new boolean[ServerData.playerCount];
    }

    private void killPlayer(int id) {
        metaData.get(id)[MetaIndexes.IS_DEAD] = 1;
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

    private int[] containsInFood(int[] pair) {
        return ServerData.foodMap.get(Converter.cantorPair(pair[0], pair[1]));
    }

    private void addToFood(int[] pair, int[] food) {
        ServerData.foodMap.put(Converter.cantorPair(pair[0], pair[1]), food);
    }

    private void removeFromFood(int[] pair) {
        ServerData.foodMap.remove(Converter.cantorPair(pair[0], pair[1]));
    }

    public boolean updatePosition() {
        int deadCount = 0;
        for (int i = 0; i < ServerData.playerCount; i++) {
            slowFlag[i] = !slowFlag[i];
            if (ServerData.speed[i] == -1 && slowFlag[i]) {
                continue;
            }

            if (metaData.get(i)[MetaIndexes.IS_DEAD] > 0) {
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

            int[] foodVal = containsInFood(next);
            if (foodVal == null) {
                int[] removed = pos.pollFirst();
                assert removed != null;
                removeFromPlayer(removed);
            } else {
                removeFromFood(foodVal);
                ServerData.positions[ServerData.playerCount].remove(foodVal);

                int[] food = new int[]{rand.nextInt(Statics.ROW - 1), rand.nextInt(Statics.COL - 1)};
                ServerData.positions[ServerData.playerCount].addLast(food);
                addToFood(food, food);

                metaData.get(i)[MetaIndexes.SCORE]++;
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
                + String.join(";", metaData.stream().map(e -> String.valueOf(e[0])).toList());
    }
}
