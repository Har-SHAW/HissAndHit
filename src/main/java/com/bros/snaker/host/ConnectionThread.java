package com.bros.snaker.host;

import com.bros.snaker.config.Statics;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionThread implements Runnable {
    int numberOfPlayers;

    ConnectionThread(int player) {
        this.numberOfPlayers = player;
    }

    @Override
    public void run() {
        try {
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
