package com.bros.HissAndHit.utils;

import com.bros.HissAndHit.GameBoard;
import com.bros.HissAndHit.GameController;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PopUps {
    public static void waitForPlayerPopup() {
        Platform.runLater(() -> {
            Text popupText = new Text("Waiting for Other players to Join.");
            Text readyText = new Text("ROOM JOINED");
            readyText.setFont(Font.font(20));
            readyText.setFill(Color.GREEN);
            VBox popupContent = new VBox(readyText, popupText);
            popupContent.setStyle("-fx-background-color: white; -fx-padding: 10px;");
            popupContent.setPrefHeight(200);
            popupContent.setPrefWidth(300);
            popupContent.setAlignment(Pos.CENTER);
            popupContent.setSpacing(30);
            GameBoard.popup.getContent().clear();
            GameBoard.popup.getContent().add(popupContent);
            GameBoard.popup.show(GameBoard.stage);
        });
    }

    public static void scorePopup() {
        Platform.runLater(() -> {
            GameBoard.popup.getContent().clear();
            Button button = new Button();
            button.setText("BACK TO HOME");
            button.setOnAction((e) -> {
                GameBoard.popup.hide();
                GameBoard.setIntro();
            });
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setStyle("-fx-background-color: white;");
            vBox.getChildren().addAll(GameController.scoreBoardObj, button);
            GameBoard.popup.getContent().add(vBox);
            GameBoard.popup.show(GameBoard.stage);
        });
    }
}
