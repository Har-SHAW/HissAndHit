package com.bros.snaker.host;

import com.bros.snaker.config.Directions;
import com.bros.snaker.config.Statics;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Data
@RequiredArgsConstructor
public class Server {
    public static Deque<int[]>[] positions;
    public static List<Directions> directions;
    @NonNull
    public static int numberOfPlayers;

    public void init() {
        positions = new Deque[numberOfPlayers + 1];
        directions = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            positions[i] = new LinkedList<>();
            positions[i].addLast(new int[]{50, 50});
            directions.add(Directions.UP);
        }
        Random rand = new Random();
        positions[numberOfPlayers] = new LinkedList<>();
        for (int i = 0; i < Statics.FOOD_SIZE; i++) {
            positions[numberOfPlayers].addLast(new int[]{rand.nextInt(Statics.COL) + 1, rand.nextInt(Statics.COL) + 1});
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
