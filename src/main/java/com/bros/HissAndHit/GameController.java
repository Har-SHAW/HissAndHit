package com.bros.HissAndHit;

import com.bros.HissAndHit.config.Statics;
import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.proto.Data;
import com.bros.HissAndHit.utils.SnakeBody;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public static final GridPane matrix = new GridPane();
    public static final double height = Screen.getPrimary().getBounds().getHeight();
    public static final double width = Screen.getPrimary().getBounds().getWidth();
    private static final List<Data.Point> resetNodes = new ArrayList<>();
    public static HBox scoreBoardObj;
    private static List<Label> scores;
    @FXML
    public HBox scoreBoard;
    @FXML
    private BorderPane root;

    public static void initScoreBoard() {
        scores = new ArrayList<>();
        for (int i = 0; i < PlayerData.playerNames.length; i++) {
            VBox vBox = new VBox();
            vBox.setPrefHeight(height / 10);
            vBox.setPrefWidth(width / 4);
            vBox.setStyle("-fx-border-width: 15; -fx-border-style: solid; -fx-border-color: " + PlayerData.playerColors[i]);
            vBox.setAlignment(Pos.CENTER);
            Label label = new Label("Name:  " + PlayerData.playerNames[i]);
            label.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
            vBox.getChildren().add(label);
            label = new Label("Score:  0");
            label.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
            vBox.getChildren().add(label);
            scores.add(label);
            scoreBoardObj.getChildren().add(vBox);
        }
    }

    public static void updateScoreBoard() {
        for (int i = 0; i < PlayerData.playerCount; i++) {
            scores.get(i).setText("Score:  " + PlayerData.metaData.getData(i).getScore());
        }
    }

    public static void update() {
        updateScoreBoard();

        if (!resetNodes.isEmpty()) {
            for (Data.Point pair : resetNodes) {
                matrix.getChildren().get(pair.getX() * Statics.COL + pair.getY())
                        .setStyle("-fx-background-color: #60ff52");
            }
            resetNodes.clear();
        }

        for (Data.Point array1D : PlayerData.food.getPointList()) {
            matrix.getChildren().get(array1D.getX() * Statics.COL + array1D.getY())
                    .setStyle("-fx-background-color: green; -fx-background-radius: 50%");
        }

        for (int i = 0; i < PlayerData.positions.getArray2SCount(); i++) {
            if (PlayerData.metaData.getData(i).getIsDead()) {
                continue;
            }

            Data.Array2D array2D = PlayerData.positions.getArray2S(i);
            int len = array2D.getArray1SCount();

            StringBuilder style = new StringBuilder("-fx-background-color: ")
                    .append(PlayerData.playerColors[i])
                    .append(";");

            ObservableList<Node> nodes = matrix.getChildren();

            nodes.get(array2D.getArray1S(0).getX() * Statics.COL + array2D.getArray1S(0).getY())
                    .setStyle(style + SnakeBody.getTail(array2D.getArray1S(1), array2D.getArray1S(0)));

            for (int j = 1; j < len - 1; j++) {
                nodes.get(array2D.getArray1S(j).getX() * Statics.COL + array2D.getArray1S(j).getY())
                        .setStyle(style + SnakeBody.getBody(array2D.getArray1S(j - 1), array2D.getArray1S(j), array2D.getArray1S(j + 1)));
            }

            nodes.get(array2D.getArray1S(len - 1).getX() * Statics.COL + array2D.getArray1S(len - 1).getY())
                    .setStyle(style + SnakeBody.getHead(array2D.getArray1S(len - 2), array2D.getArray1S(len - 1)));

            resetNodes.addAll(array2D.getArray1SList());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        matrix.getChildren().clear();

        for (int row = 0; row < Statics.ROW; row++) {
            for (int col = 0; col < Statics.COL; col++) {
                Pane pane = new Pane();
                pane.setPrefSize(height / Statics.ROW, width / Statics.COL);
                pane.setStyle("-fx-background-color: #60ff52");
                matrix.add(pane, col, row);
            }
        }

        matrix.setStyle("-fx-background-color: #60ff52;");
        root.setCenter(matrix);

        scoreBoardObj = scoreBoard;
    }
}