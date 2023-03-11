package com.bros.snaker;

import com.bros.snaker.host.Server;
import com.bros.snaker.player.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.time.Duration;

public class HelloController {
    @FXML
    private Label welcomeText;

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
}