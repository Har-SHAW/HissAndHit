package com.bros.snaker;

import com.bros.snaker.config.Statics;
import com.bros.snaker.data.PlayerData;
import com.bros.snaker.data.ServerData;
import com.bros.snaker.host.Server;
import com.bros.snaker.player.Player;
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
        if (!resetNodes.isEmpty()) {
            for (int[] pair : resetNodes) {
                Pane pane = (Pane) matrix.getChildren().get(pair[0] * Statics.COL + pair[1]);
                pane.setStyle("-fx-background-color: green");
                pane.requestLayout();
            }
            resetNodes.clear();
        }

        for (int[] pair : PlayerData.positions[PlayerData.positions.length - 1]) {
            try {
                Pane pane = (Pane) matrix.getChildren().get(pair[0] * Statics.COL + pair[1]);
                pane.setStyle("-fx-background-color: yellow; -fx-background-radius: 50%");
                pane.requestLayout();
            } catch (Exception e) {
                System.out.println(pair[0] + " " + pair[1]);
            }
        }

        for (int i = 0; i < PlayerData.positions.length - 1; i++) {
            for (int[] pair : PlayerData.positions[i]) {
                Pane pane = (Pane) matrix.getChildren().get(pair[0] * Statics.COL + pair[1]);
                pane.setStyle("-fx-background-color: black; -fx-background-radius: 50% 50% 0 0");
                pane.requestLayout();
            }
            resetNodes.add(PlayerData.positions[i].getFirst());
        }
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