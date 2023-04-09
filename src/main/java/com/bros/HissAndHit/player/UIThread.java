package com.bros.HissAndHit.player;

import com.bros.HissAndHit.GameController;
import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.utils.Converter;

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
