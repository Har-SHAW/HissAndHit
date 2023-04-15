package com.bros.HissAndHit.scenes;

import com.bros.HissAndHit.GameBoard;
import com.bros.HissAndHit.config.Scenes;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;

public class GameScene {
    public static void setGame() {
        FXMLLoader fxmlLoader = new FXMLLoader(GameBoard.class.getResource(Scenes.GAME_SCENE));

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

        GameBoard.popup.getContent().clear();
        GameBoard.popup.getContent().addAll(popupContent);

        GameBoard.scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (GameBoard.controllerInput != null) {
                if (event.getCode().toString().equals("R")) {
                    readyText.setFill(Color.GREEN);
                    readyText.setFont(Font.font(20));
                    readyText.setText("READY");
                    popupText.setText("Waiting for other players.");
                    GameBoard.controllerInput.println("READY");
                } else if (event.getCode() == KeyCode.SPACE) {
                    GameBoard.controllerInput.println("S_P");
                } else if (event.getCode() == KeyCode.SHIFT) {
                    GameBoard.controllerInput.println("L_P");
                } else {
                    GameBoard.controllerInput.println(event.getCode().toString());
                }
            }
        });

        GameBoard.scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (GameBoard.controllerInput != null) {
                if (event.getCode() == KeyCode.SPACE) {
                    GameBoard.controllerInput.println("S_R");
                } else if (event.getCode() == KeyCode.SHIFT) {
                    GameBoard.controllerInput.println("L_R");
                }
            }
        });

        try {
            GameBoard.stage.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GameBoard.popup.show(GameBoard.stage);
    }
}
