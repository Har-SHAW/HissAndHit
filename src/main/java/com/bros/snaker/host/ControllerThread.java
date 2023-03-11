package com.bros.snaker.host;

import com.bros.snaker.config.Directions;
import com.bros.snaker.config.Statics;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

@RequiredArgsConstructor
public class ControllerThread implements Runnable {
    @NonNull
    int playerId;
    ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(Statics.PORT + playerId);
            Socket clientSocket = serverSocket.accept();
            System.out.println(playerId + " controller connected");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                Directions response = Directions.valueOf(in.readLine());
                Server.directions.set(playerId - 1, response);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
