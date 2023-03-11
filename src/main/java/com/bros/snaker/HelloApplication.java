package com.bros.snaker;

import com.bros.snaker.config.Directions;
import com.bros.snaker.player.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            try {
                PrintWriter out = new PrintWriter(Player.controllerSocket.getOutputStream(), true);
                if (event.getCode().toString().equals("W")) {
                    out.println(Directions.UP);
                } else if (event.getCode().toString().equals("A")) {
                    out.println(Directions.LEFT);
                } else if (event.getCode().toString().equals("D")) {
                    out.println(Directions.RIGHT);
                } else if (event.getCode().toString().equals("S")) {
                    out.println(Directions.DOWN);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        stage.setTitle("Snake");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}