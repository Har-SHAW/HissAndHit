package com.bros.HissAndHit.utils;

import com.bros.HissAndHit.GameBoard;
import com.bros.HissAndHit.GameController;
import com.bros.HissAndHit.data.PlayerData;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PopUps {
    public static void waitForPlayerPopup() {
        Platform.runLater(() -> {
            GameBoard.stage.getScene().getRoot().setStyle("-fx-opacity: 0.5");
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
            GameBoard.stage.getScene().getRoot().setStyle("-fx-opacity: 0.5");
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

//            PlayerData.metaData.getDataList().sort((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()));

            for (int i = 0; i < PlayerData.playerNames.length; i++) {
                VBox vBox1 = new VBox();
                vBox1.setPrefHeight(GameController.height / 10);
                vBox1.setPrefWidth(GameController.width / 4);
                vBox1.setStyle("-fx-border-width: 10; -fx-border-style: solid; -fx-border-color: " + PlayerData.playerColors[i]);
                vBox1.setAlignment(Pos.CENTER);
                Label label = new Label("Name:  " + PlayerData.playerNames[i]);
                label.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
                if (!PlayerData.metaData.getData(i).getIsDead()) {
                    label = new Label("WINNER");
                    label.setStyle("-fx-font-size: 40; -fx-font-weight: bold;");
                    label.setTextFill(Color.GREEN);
                    vBox1.getChildren().add(label);
                }
                label = new Label("Name:  " + PlayerData.playerNames[i]);
                label.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
                vBox1.getChildren().add(label);
                label = new Label("Score:  " + PlayerData.metaData.getData(i).getScore());
                label.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
                vBox1.getChildren().add(label);
                vBox.getChildren().add(vBox1);
            }

            vBox.getChildren().addAll(button);
            GameBoard.popup.getContent().add(vBox);
            GameBoard.popup.show(GameBoard.stage);
        });
    }
}
