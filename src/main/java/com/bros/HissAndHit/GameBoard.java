package com.bros.HissAndHit;

import com.bros.HissAndHit.config.Scenes;
import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.utils.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GameBoard extends Application {
    public static PrintWriter controllerInput;
    public static Stage stage;
    public static Popup popup;
    public static Scene scene;

    public static void main(String[] args) {
        launch();
    }

    public static void setCreateRoom() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameBoard.class.getResource(Scenes.CREATE_ROOM));
        stage.getScene().setRoot(fxmlLoader.load());
    }

    public static void setJoinRoom() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameBoard.class.getResource(Scenes.JOIN_ROOM));
        stage.getScene().setRoot(fxmlLoader.load());
    }

    public static void setIntro() {
        FXMLLoader fxmlLoader = new FXMLLoader(GameBoard.class.getResource(Scenes.INTRO));
        try {
            GameBoard.scene = new Scene(fxmlLoader.load(), 1920, 1080);
            GameBoard.scene.setFill(Paint.valueOf("black"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GameBoard.stage.setTitle("Hiss and Hit");
        GameBoard.stage.setScene(GameBoard.scene);
    }

    @Override
    public void start(Stage stage) {
        GameBoard.popup = new Popup();
        GameBoard.stage = stage;
        setIntro();
        GameBoard.stage.show();
    }

    @Override
    public void init() throws SocketException, UnknownHostException {
        String ipAddress = Network.getIpAddress();
        assert ipAddress != null;
        PlayerData.IPAddress = ipAddress;
    }
}