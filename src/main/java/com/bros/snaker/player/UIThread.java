package com.bros.snaker.player;

import com.bros.snaker.GameController;
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
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(UISocket.getInputStream()));
            while (true) {
                PlayerData.positions = Converter.fromString(reader.readLine());
                GameController.update();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
