package com.bros.HissAndHit;

import com.bros.HissAndHit.config.MetaIndexes;
import com.bros.HissAndHit.config.Statics;
import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.utils.SnakeBody;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public static final GridPane matrix = new GridPane();
    private static final List<int[]> resetNodes = new ArrayList<>();
    private static final double height = Screen.getPrimary().getBounds().getHeight();
    private static final double width = Screen.getPrimary().getBounds().getWidth();
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
            vBox.setPrefWidth(width / PlayerData.playerNames.length);
            vBox.setStyle("-fx-background-color: #" + Integer.toHexString(PlayerData.playerColors.get(i)));
            vBox.getChildren().add(new Label("Name: " + PlayerData.playerNames[i]));
            Label label = new Label("Score: 0");
            vBox.getChildren().add(label);
            scores.add(label);
            scoreBoardObj.getChildren().add(vBox);
        }
        scoreBoardObj.requestLayout();
    }

    public static void updateScoreBoard() {
        for (int i = 0; i < PlayerData.playerCount; i++) {
            scores.get(i).setText("Score: " + PlayerData.positions[PlayerData.playerCount + 1][i][MetaIndexes.SCORE]);
            scores.get(i).requestLayout();
        }
    }

    public static void update() {
        int players = PlayerData.positions.length - 2;
        int food = PlayerData.positions.length - 1;
        int meta = PlayerData.positions.length;

        updateScoreBoard();

        if (!resetNodes.isEmpty()) {
            for (int[] pair : resetNodes) {
                matrix.getChildren().get(pair[0] * Statics.COL + pair[1])
                        .setStyle("-fx-background-color: #60ff52");
            }
            resetNodes.clear();
        }

        for (int[] pair : PlayerData.positions[food - 1]) {
            matrix.getChildren().get(pair[0] * Statics.COL + pair[1])
                    .setStyle("-fx-background-color: green; -fx-background-radius: 50%");
        }

        for (int i = 0; i < players; i++) {
            if (PlayerData.positions[meta - 1][i][MetaIndexes.IS_DEAD] > 0) {
                continue;
            }

            int len = PlayerData.positions[i].length;
            int[][] playerPositions = PlayerData.positions[i];

            StringBuilder style = new StringBuilder("-fx-background-color: ")
                    .append("#").append(Integer.toHexString(PlayerData.positions[meta - 1][i][MetaIndexes.COLOR]))
                    .append(";");

            ObservableList<Node> nodes = matrix.getChildren();

            nodes.get(playerPositions[0][0] * Statics.COL + playerPositions[0][1])
                    .setStyle(style + SnakeBody.getTail(playerPositions[1], playerPositions[0]));

            for (int j = 1; j < len - 1; j++) {
                nodes.get(playerPositions[j][0] * Statics.COL + playerPositions[j][1])
                        .setStyle(style + SnakeBody.getBody(playerPositions[j - 1], playerPositions[j], playerPositions[j + 1]));
            }

            nodes.get(playerPositions[len - 1][0] * Statics.COL + playerPositions[len - 1][1])
                    .setStyle(style + SnakeBody.getHead(playerPositions[len - 2], playerPositions[len - 1]));

            Collections.addAll(resetNodes, PlayerData.positions[i]);
        }

        matrix.requestLayout();
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

        GameBoard.controllerInput.println("LOAD");
    }
}