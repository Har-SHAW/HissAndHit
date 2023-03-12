package com.bros.snaker.player;

import com.bros.snaker.config.Statics;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Deque;

public class Player {
    public static Deque<int[]>[] positions;
    public static Socket controllerSocket;
    public static Socket UISocket;

    public void start() throws IOException {
        UISocket = new Socket("localhost", Statics.PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(UISocket.getInputStream()));
        int controllerPort = Integer.parseInt(in.readLine());

        controllerSocket = new Socket("localhost", Statics.PORT + controllerPort);

        Thread thread = new Thread(new UIThread(UISocket), "Player UI Thread");
        thread.start();
    }
}
