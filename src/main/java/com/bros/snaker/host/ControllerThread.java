package com.bros.snaker.host;

import com.bros.snaker.config.Directions;
import com.bros.snaker.config.Statics;
import com.bros.snaker.data.ServerData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class ControllerThread implements Runnable {
    int playerId;
    int port;
    ServerSocket serverSocket;

    ControllerThread(int i) {
        this.playerId = i;
        this.port = Statics.PORT + i + 1;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            System.out.println(playerId + 1 + " controller connected");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                Integer data = Directions.data.get(in.readLine());
                if (data != null) {
                    ServerData.directions[playerId] = data;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
