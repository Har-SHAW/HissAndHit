package com.bros.snaker;

import com.bros.snaker.config.Statics;
import com.bros.snaker.host.Server;
import com.bros.snaker.player.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    @FXML
    private BorderPane root;

    public static void update() {
        if (!resetNodes.isEmpty()) {
            for (int[] pair : resetNodes) {
                Pane pane = (Pane) matrix.getChildren().get(pair[0] * Statics.ROW + pair[1]);
                pane.setStyle("-fx-background-color: green");
                pane.requestLayout();
            }
            resetNodes.clear();
        }

        for (int[] pair : Player.positions[Player.positions.length - 1]) {
            Pane pane = (Pane) matrix.getChildren().get(pair[0] * Statics.ROW + pair[1]);
            pane.setStyle("-fx-background-color: yellow");
            pane.requestLayout();
        }

        for (int i = 0; i < Player.positions.length - 1; i++) {
            for (int[] pair : Player.positions[i]) {
                Pane pane = (Pane) matrix.getChildren().get(pair[0] * Statics.ROW + pair[1]);
                pane.setStyle("-fx-background-color: black");
                pane.requestLayout();
            }
            resetNodes.add(Player.positions[i].getFirst());
        }
    }


    public void onCreateRoom() throws InterruptedException {
        Server server = new Server();
        Server.numberOfPlayers = 1;
        server.init();
        server.start();
    }

    public void onJoinRoom() throws IOException {
        Player player = new Player();
        player.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int row = 0; row < Statics.ROW; row++) {
            for (int col = 0; col < Statics.COL; col++) {
                Pane pane = new Pane();
                pane.setPrefSize(10, 10);
                pane.setStyle("-fx-background-color: green");
                matrix.add(pane, col, row);
            }
        }

        root.setCenter(matrix);
    }
}