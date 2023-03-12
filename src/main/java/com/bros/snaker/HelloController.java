package com.bros.snaker;

import com.bros.snaker.host.Server;
import com.bros.snaker.player.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public static GridPane matrix;
    @FXML
    private BorderPane root;

    public static void update() {
        int rows = 100;
        Pane pane = (Pane) matrix.getChildren().get(Player.positions.get(0).getFirst().getKey() * rows + Player.positions.get(0).getFirst().getValue());
        pane.getChildren().get(0).setOpacity(0);
        pane.requestLayout();
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
                Rectangle rectangle1 = new Rectangle(10, 10, Color.GREEN);
                Pane pane = new Pane(rectangle1);
                matrix.add(pane, col, row);
            }
        }

        root.setCenter(matrix);
        System.out.println("plane init");
    }
}