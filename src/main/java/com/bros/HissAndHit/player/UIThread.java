package com.bros.HissAndHit.player;

import com.bros.HissAndHit.GameBoard;
import com.bros.HissAndHit.GameController;
import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.utils.Compressor;
import com.bros.HissAndHit.utils.PopUps;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

public class UIThread implements Runnable {
    private final BufferedReader reader;
    private final InputStream inputStream;

    UIThread(Socket socket) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.inputStream = socket.getInputStream();
    }

    @Override
    public void run() {
        try {
            PopUps.waitForPlayerPopup();
            GameBoard.controllerInput.println("LOAD");

            String data = reader.readLine();
            PlayerData.playerNames = data.split("\\|")[0].split(";");
            PlayerData.playerCount = PlayerData.playerNames.length;
            PlayerData.playerColors = data.split("\\|")[1].split(";");

            Platform.runLater(() -> {
                GameBoard.popup.hide();
                GameBoard.setGame();
                GameController.initScoreBoard();
            });

            Compressor.deserialize(readSmall());
            Platform.runLater(GameController::update);

            Compressor.deserialize(readSmall());
            Platform.runLater(GameBoard.popup::hide);
            Platform.runLater(GameController::update);

            byte[] bytes;
            while ((bytes = readSmall()) != null) {
                Compressor.deserialize(bytes);
                Platform.runLater(GameController::update);
            }
            PopUps.scorePopup();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] readSmall() throws IOException {
        byte[] buffer = new byte[409600];
        int read = inputStream.read(buffer);
        if (read == -1) {
            return null;
        }
        return Arrays.copyOf(buffer, read);
    }
}
