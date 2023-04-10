package com.bros.HissAndHit;

import com.bros.HissAndHit.config.Scenes;
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
    public static Stage stage;

    public static void main(String[] args) {
        launch();
    }

    public static void setCreateRoom() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameBoard.class.getResource(Scenes.CREATE_ROOM));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 1920);

        GameBoard.stage.setScene(scene);
        GameBoard.stage.show();
    }

    public static void setJoinRoom() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameBoard.class.getResource(Scenes.JOIN_ROOM));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 1920);

        GameBoard.stage.setScene(scene);
        GameBoard.stage.show();
    }

    public static void setGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameBoard.class.getResource(Scenes.GAME_SCENE));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 1920);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (controllerInput != null) {
                if (event.getCode().toString().equals("R")) {
                    controllerInput.println("READY");
                }
                controllerInput.println(event.getCode().toString());
            }
        });

        GameBoard.stage.setScene(scene);
        GameBoard.stage.show();
    }

    @Override
    public void start(Stage stage) throws IOException {
        GameBoard.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(GameBoard.class.getResource(Scenes.INTRO));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 1920);

        GameBoard.stage.setTitle("Hiss and Hit");
        GameBoard.stage.setScene(scene);
        GameBoard.stage.show();
    }

    @Override
    public void init() throws SocketException, UnknownHostException {
        String ipAddress = Network.getIpAddress();
        assert ipAddress != null;
        PlayerData.IPAddress = ipAddress;
    }
}