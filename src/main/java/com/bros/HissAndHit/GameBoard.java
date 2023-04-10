package com.bros.HissAndHit;

import com.bros.HissAndHit.config.Scenes;
import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.utils.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GameBoard extends Application {
    public static PrintWriter controllerInput;
    public static Stage stage;
    public static Popup popup = new Popup();

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

        Text popupText = new Text("Press 'R' Key on the keyboard to get ready.");
        Text readyText = new Text("NOT READY");
        readyText.setFont(Font.font(20));
        readyText.setFill(Color.RED);
        VBox popupContent = new VBox(popupText, readyText);
        popupContent.setStyle("-fx-background-color: white; -fx-padding: 10px;");
        popupContent.setPrefHeight(200);
        popupContent.setPrefWidth(300);
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setSpacing(30);

        popup.getContent().addAll(popupContent);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (controllerInput != null) {
                if (event.getCode().toString().equals("R")) {
                    readyText.setFill(Color.GREEN);
                    readyText.setFont(Font.font(20));
                    readyText.setText("READY");
                    popupText.setText("Waiting for other players.");
                    controllerInput.println("READY");
                } else if (event.getCode().toString().equals("SPACE")) {
                    controllerInput.println("S_P");
                } else {
                    controllerInput.println(event.getCode().toString());
                }
            }
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (controllerInput != null) {
                if (event.getCode().toString().equals("SPACE")) {
                    controllerInput.println("S_R");
                }
            }
        });

        GameBoard.stage.setScene(scene);
        GameBoard.stage.show();

        popup.show(GameBoard.stage);
    }

    public static void setIntro() {
        FXMLLoader fxmlLoader = new FXMLLoader(GameBoard.class.getResource(Scenes.INTRO));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 1080, 1920);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GameBoard.stage.setTitle("Hiss and Hit");
        GameBoard.stage.setScene(scene);
        GameBoard.stage.show();
    }

    @Override
    public void start(Stage stage) {
        GameBoard.stage = stage;
        setIntro();
    }

    @Override
    public void init() throws SocketException, UnknownHostException {
        String ipAddress = Network.getIpAddress();
        assert ipAddress != null;
        PlayerData.IPAddress = ipAddress;
    }
}