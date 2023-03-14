package com.bros.snaker.host;

import com.bros.snaker.config.Directions;
import com.bros.snaker.config.Statics;

import java.util.*;

public class Server {
    public static Deque<int[]>[] positions;
    public static List<Directions> directions;
    public static int numberOfPlayers;

    public void init() {
        positions = new Deque[numberOfPlayers + 1];
        directions = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            positions[i] = new LinkedList<>();
            positions[i].addLast(new int[]{20, 20});
            directions.add(Directions.UP);
        }
        Random rand = new Random();
        positions[numberOfPlayers] = new LinkedList<>();
        for (int i = 0; i < Statics.FOOD_SIZE; i++) {
            positions[numberOfPlayers].addLast(new int[]{rand.nextInt(1, Statics.ROW) - 1, rand.nextInt(1, Statics.COL) - 1});
        }
    }

    public void start() {
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            threads.add(new Thread(new ControllerThread(i), "Controller Player-" + i));
        }
        for (int i = 0; i < numberOfPlayers; i++) {
            threads.get(i).start();
        }

        Thread thread = new Thread(new ConnectionThread(numberOfPlayers), "Connect Thread");
        thread.start();
    }
}
