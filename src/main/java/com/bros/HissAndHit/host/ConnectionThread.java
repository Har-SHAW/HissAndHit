package com.bros.HissAndHit.host;

import com.bros.HissAndHit.config.Statics;
import com.bros.HissAndHit.data.ServerData;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionThread implements Runnable {
    int numberOfPlayers;

    ConnectionThread(int player) {
        this.numberOfPlayers = player;
    }

    @Override
    public void run() {
        try {
            List<Thread> threads = new ArrayList<>();
            List<ControllerThread> controllerThreads = new ArrayList<>();

            for (int i = 0; i < ServerData.playerCount; i++) {
                ControllerThread controllerThread = new ControllerThread(i);
                controllerThreads.add(controllerThread);
                threads.add(new Thread(controllerThread, "Controller Player-" + (i + 1)));
            }
            for (int i = 0; i < ServerData.playerCount; i++) {
                threads.get(i).start();
            }

            ServerSocket serverSocket = new ServerSocket(Statics.PORT);
            Socket[] clients = new Socket[numberOfPlayers];

            for (int i = 1; i <= numberOfPlayers; i++) {
                Socket clientSocket = serverSocket.accept();
                clients[i - 1] = clientSocket;
                System.out.println("Player Connected " + i);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(i);
            }

            serverSocket.close();

            Thread thread = new Thread(new MainThread(clients), "Boss Thread");
            thread.start();
            thread.join();

            for (ControllerThread controllerThread : controllerThreads) {
                controllerThread.client.close();
            }

            for (Thread th : threads) {
                th.join();
            }
            System.out.println("closed");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
