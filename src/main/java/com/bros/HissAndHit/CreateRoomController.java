package com.bros.HissAndHit;

import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.data.ServerData;
import com.bros.HissAndHit.host.Server;
import com.bros.HissAndHit.utils.Converter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateRoomController implements Initializable {
    @FXML
    public TextField playerCount;
    @FXML
    public Label roomCode;
    @FXML
    public Button joinRoomButton;
    @FXML
    public Button createRoomButton;

    public void createRoom() throws InterruptedException {
        Server server = new Server();
        ServerData.playerCount = Integer.parseInt(playerCount.getText());
        PlayerData.roomCode = String.valueOf(Converter.ipv4ToInt(PlayerData.IPAddress));
        server.init();
        server.start();
        Thread.sleep(1000);
        roomCode.setText("Room Code: " + PlayerData.roomCode);
        joinRoomButton.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        joinRoomButton.setDisable(true);
    }

    public void joinRoom() throws IOException {
        GameBoard.setJoinRoom();
    }
}
