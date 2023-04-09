package com.bros.HissAndHit;

import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.utils.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GameBoard extends Application {
    public static PrintWriter controllerInput;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameBoard.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (controllerInput != null) {
                controllerInput.println(event.getCode().toString());
            }
        });

        stage.setTitle("Snake");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws SocketException, UnknownHostException {
        String ipAddress = Network.getIpAddress();
        System.out.printf(ipAddress);
        assert ipAddress != null;
        PlayerData.IPAddress = ipAddress;
        String[] blocks = ipAddress.split("\\.");
        if (blocks.length != 4) {
            throw new IllegalArgumentException("Invalid IPv4 address");
        }
        PlayerData.IPAddressPrefix = blocks[0] + "." + blocks[1] + "." + blocks[2];
        PlayerData.roomCode = blocks[3];
    }
}