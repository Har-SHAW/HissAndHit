package com.bros.snaker.host;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Server {
    @NonNull
    private int numberOfPlayers;

    private List<List<Integer>> positions;

    public void init() {
        positions = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            positions.add(new ArrayList<>());
        }
    }

    public void start() {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++) {
            Runnable runnable = new ControllerThread(i + 1);
            threads.add(new Thread(runnable, "player-" + (i + 1)));
        }
        for (int i = 0; i < numberOfPlayers; i++) {
            threads.get(i).start();
        }

        System.out.println("controllers done");

        Thread thread = new Thread(new ConnectionThread(numberOfPlayers), "Connect Thread");
        thread.start();
    }
}
