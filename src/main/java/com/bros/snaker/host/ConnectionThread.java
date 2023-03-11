package com.bros.snaker.host;

import com.bros.snaker.config.Statics;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

@RequiredArgsConstructor
public class ConnectionThread implements Runnable {
    @NonNull
    int numberOfPlayers;

    @Override
    public void run() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numberOfPlayers + 1);

        try {
            ServerSocket serverSocket = new ServerSocket(Statics.PORT);
            for (int i = 1; i <= numberOfPlayers; i++) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Player Connected " + i);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(i);
                Thread thread = new Thread(new MainThread(i, clientSocket, cyclicBarrier), "Main Thread Player-" + i);
                thread.start();
            }

            Thread thread = new Thread(new PingThread(cyclicBarrier), "Ping Thread");
            thread.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
