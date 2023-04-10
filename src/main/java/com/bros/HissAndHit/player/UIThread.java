package com.bros.HissAndHit.player;

import com.bros.HissAndHit.GameController;
import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.utils.Converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

public class UIThread implements Runnable {
    Socket UISocket;

    BufferedReader reader;

    UIThread(Socket socket) throws IOException {
        this.UISocket = socket;
        this.reader = new BufferedReader(new InputStreamReader(UISocket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String data = reader.readLine();

            PlayerData.playerNames = data.split("\\|")[0].split(";");
            PlayerData.playerCount = PlayerData.playerNames.length;
            PlayerData.playerColors = Arrays.stream(data.split("\\|")[1].split(";")).map(Integer::parseInt).toList();

            GameController.initScoreBoard();

            while (true) {
                PlayerData.positions = Converter.fromString(reader.readLine());
                GameController.update();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
