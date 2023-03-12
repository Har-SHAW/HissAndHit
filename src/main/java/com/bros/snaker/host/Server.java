package com.bros.snaker.host;

import com.bros.snaker.config.Directions;
import javafx.util.Pair;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Server {
    public static List<Deque<Pair<Integer, Integer>>> positions;
    public static List<Directions> directions;
    @NonNull
    private int numberOfPlayers;

    public void init() {
        positions = new ArrayList<>();
        directions = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            positions.add(new LinkedList<>());
            positions.get(i).addLast(new Pair<>(50, 50));
            directions.add(Directions.UP);
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

        System.out.println("controllers done");

        Thread thread = new Thread(new ConnectionThread(numberOfPlayers), "Connect Thread");
        thread.start();
    }
}
