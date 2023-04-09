package com.bros.HissAndHit;

import com.bros.HissAndHit.config.MetaIndexes;
import com.bros.HissAndHit.config.Statics;
import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.data.ServerData;
import com.bros.HissAndHit.host.Server;
import com.bros.HissAndHit.player.Player;
import com.bros.HissAndHit.utils.SnakeBody;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

import java.io.IOException;
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
    @FXML
    public TextField playerCount;
    @FXML
    public TextField roomCode;
    @FXML
    public Label roomCodeLabel;
    @FXML
    private BorderPane root;

    public static void update() {
        int players = PlayerData.positions.length - 2;
        int food = PlayerData.positions.length - 1;
        int meta = PlayerData.positions.length;

        if (!resetNodes.isEmpty()) {
            for (int[] pair : resetNodes) {
                matrix.getChildren().get(pair[0] * Statics.COL + pair[1])
                        .setStyle("-fx-background-color: green");
            }
            resetNodes.clear();
        }

        for (int[] pair : PlayerData.positions[food - 1]) {
            matrix.getChildren().get(pair[0] * Statics.COL + pair[1])
                    .setStyle("-fx-background-color: yellow; -fx-background-radius: 50%");
        }

        for (int i = 0; i < players; i++) {
            if (PlayerData.positions[meta - 1][i][MetaIndexes.IS_DEAD] > 0) {
                Collections.addAll(resetNodes, PlayerData.positions[i]);
                continue;
            }
            int len = PlayerData.positions[i].length;
            int[][] playerPositions = PlayerData.positions[i];

            StringBuilder style = new StringBuilder("-fx-background-color: ")
                    .append("#").append(Integer.toHexString(PlayerData.positions[meta - 1][i][MetaIndexes.COLOR]))
                    .append(";");

            matrix.getChildren()
                    .get(playerPositions[0][0] * Statics.COL + playerPositions[0][1])
                    .setStyle(style + SnakeBody.getTail(playerPositions[1], playerPositions[0]));

            for (int j = 1; j < len - 1; j++) {
                matrix.getChildren()
                        .get(playerPositions[j][0] * Statics.COL + playerPositions[j][1])
                        .setStyle(style + SnakeBody.getBody(playerPositions[j - 1], playerPositions[j], playerPositions[j + 1]));
            }

            matrix.getChildren()
                    .get(playerPositions[len - 1][0] * Statics.COL + playerPositions[len - 1][1])
                    .setStyle(style + SnakeBody.getHead(playerPositions[len - 2], playerPositions[len - 1]));

            resetNodes.add(playerPositions[0]);
        }

        matrix.requestLayout();
    }


    public void onCreateRoom() throws InterruptedException {
        Server server = new Server();
        ServerData.playerCount = Integer.parseInt(playerCount.getText());
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
                pane.setPrefSize(height / Statics.ROW, width / Statics.COL);
                pane.setStyle("-fx-background-color: green");
                matrix.add(pane, col, row);
            }
        }

        matrix.setStyle("-fx-background-color: green;");
        root.setCenter(matrix);
    }
}