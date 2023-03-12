package com.bros.snaker;

import com.bros.snaker.host.Server;
import com.bros.snaker.player.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    static List<int[]> resetNodes = new ArrayList<>();
    public static GridPane matrix;
    @FXML
    private BorderPane root;

    public static void update() {
        int rows = 100;

        if (!resetNodes.isEmpty()) {
            for (int[] pair : resetNodes) {
                Pane pane = (Pane) matrix.getChildren().get(pair[0] * rows + pair[1]);
                pane.setStyle("-fx-background-color: green");
                pane.requestLayout();
            }
            resetNodes.clear();
        }

        for (Deque<int[]> player : Player.positions) {
            for (int[] pair : player) {
                Pane pane = (Pane) matrix.getChildren().get(pair[0] * rows + pair[1]);
                pane.setStyle("-fx-background-color: black");
                pane.requestLayout();
            }
            resetNodes.add(player.getFirst());
        }
    }


    public void onCreateRoom() throws IOException, InterruptedException {
        Server server = new Server(1);
        server.init();
        server.start();
        Thread.sleep(Duration.ofSeconds(1));
        System.out.println("Connect Attempt");
        Player player = new Player();
        player.start();
    }

    public void onJoinRoom() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        matrix = new GridPane();
        int rows = 100;
        int cols = 100;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Pane pane = new Pane();
                pane.setPrefSize(10, 10);
                pane.setStyle("-fx-background-color: green");
                matrix.add(pane, col, row);
            }
        }

        root.setCenter(matrix);
        System.out.println("plane init");
    }
}