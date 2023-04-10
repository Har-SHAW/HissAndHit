package com.bros.HissAndHit.player;

import com.bros.HissAndHit.GameBoard;
import com.bros.HissAndHit.GameController;
import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.utils.Converter;
import javafx.application.Platform;

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

            Platform.runLater(GameController::initScoreBoard);

            PlayerData.positions = Converter.fromString(reader.readLine());
            Platform.runLater(GameController::update);

            PlayerData.positions = Converter.fromString(reader.readLine());
            Platform.runLater(GameBoard.popup::hide);
            Platform.runLater(GameController::update);

            while ((data = reader.readLine()) != null) {
                PlayerData.positions = Converter.fromString(data);
                Platform.runLater(GameController::update);
            }

            Platform.runLater(GameBoard::setIntro);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
