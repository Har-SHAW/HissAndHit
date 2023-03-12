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
    public static Deque<int[]>[] positions;
    public static List<Directions> directions;
    @NonNull
    private int numberOfPlayers;

    public void init() {
        positions = new Deque[numberOfPlayers];
        directions = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            positions[i] = new LinkedList<>();
            int[] arr = {50, 50};
            positions[i].addLast(arr);
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
