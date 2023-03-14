package com.bros.snaker.player;

import com.bros.snaker.HelloController;
import com.bros.snaker.data.PlayerData;
import com.bros.snaker.utils.Converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class UIThread implements Runnable {
    Socket UISocket;

    UIThread(Socket socket) {
        this.UISocket = socket;
    }

    @Override
    public void run() {
        int bytesRead = 0;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(UISocket.getInputStream()));
            while (true) {
                String message = reader.readLine();
                PlayerData.positions = Converter.fromString(message);
                HelloController.update();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Data Loss");
            System.out.println(bytesRead);
        }

    }
}
