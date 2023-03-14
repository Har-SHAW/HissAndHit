package com.bros.snaker;

import com.bros.snaker.config.Statics;
import com.bros.snaker.data.PlayerData;
import com.bros.snaker.data.ServerData;
import com.bros.snaker.host.Server;
import com.bros.snaker.player.Player;
import com.bros.snaker.utils.SnakeBody;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public static GridPane matrix = new GridPane();
    static List<int[]> resetNodes = new ArrayList<>();
    public TextField playerCount;
    public TextField roomCode;
    public Label roomCodeLabel;
    @FXML
    private BorderPane root;

    public static void update() {
        int players = PlayerData.positions.length - 2;
        int food = PlayerData.positions.length - 1;
        int meta = PlayerData.positions.length;

        if (!resetNodes.isEmpty()) {
            for (int[] pair : resetNodes) {
                Pane pane = (Pane) matrix.getChildren().get(pair[0] * Statics.COL + pair[1]);
                pane.setStyle("-fx-background-color: green");
                pane.requestLayout();
            }
            resetNodes.clear();
        }

        for (int[] pair : PlayerData.positions[food - 1]) {
            Pane pane = (Pane) matrix.getChildren().get(pair[0] * Statics.COL + pair[1]);
                pane.setStyle("-fx-background-color: yellow; -fx-background-radius: 50%");
                pane.requestLayout();
        }

        for (int i = 0; i < players; i++) {
            int len = PlayerData.positions[i].length;
            int[][] playerPositions = PlayerData.positions[i];
            String color = "#" + Integer.toHexString(PlayerData.positions[meta - 1][i][0]);

            StringBuilder style = new StringBuilder("-fx-background-color: ").append(color)
                    .append(";");

            Pane pane = (Pane) matrix.getChildren().get(playerPositions[0][0] * Statics.COL + playerPositions[0][1]);
            pane.setStyle(style + SnakeBody.getTail(playerPositions[1], playerPositions[0]));
            pane.requestLayout();

            for (int j = 1; j < len - 1; j++) {
                pane = (Pane) matrix.getChildren()
                        .get(playerPositions[j][0] * Statics.COL + playerPositions[j][1]);
                pane.setStyle(style + SnakeBody.getBody(playerPositions[j - 1], playerPositions[j], playerPositions[j + 1]));
                pane.requestLayout();
            }

            pane = (Pane) matrix.getChildren()
                    .get(playerPositions[len - 1][0] * Statics.COL + playerPositions[len - 1][1]);
            pane.setStyle(style + SnakeBody.getHead(playerPositions[len - 2], playerPositions[len - 1]));
            pane.requestLayout();

            resetNodes.add(playerPositions[0]);
        }
        matrix.requestLayout();
    }


    public void onCreateRoom() throws InterruptedException {
        Server server = new Server();
        ServerData.numberOfPlayers = Integer.parseInt(playerCount.getText());
        server.init();
        server.start();
        Thread.sleep(1000);
        roomCodeLabel.setText(PlayerData.roomCode);
    }

    public void onJoinRoom() throws IOException {
        PlayerData.roomCode = roomCode.getText();
        Player player = new Player();
        player.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int row = 0; row < Statics.ROW; row++) {
            for (int col = 0; col < Statics.COL; col++) {
                Pane pane = new Pane();
                pane.setPrefSize(Statics.SNAKE_CELL_HEIGHT, Statics.SNAKE_CELL_WIDTH);
                pane.setStyle("-fx-background-color: green");
                matrix.add(pane, col, row);
            }
        }

        root.setCenter(matrix);
    }
}