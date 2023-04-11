package com.bros.HissAndHit;

import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.player.Player;
import com.bros.HissAndHit.utils.Converter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JoinRoomController implements Initializable {
    @FXML
    public Button button;
    @FXML
    public ColorPicker playerColor;
    @FXML
    public TextField playerName;
    @FXML
    public TextField roomCode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (PlayerData.roomCode != null) {
            roomCode.setText(PlayerData.roomCode);
            roomCode.setDisable(true);
        }
    }

    public void joinRoom() throws IOException {
        PlayerData.roomCode = roomCode.getText();
        PlayerData.roomIpAddress = Converter.intToIpv4(Integer.parseInt(roomCode.getText()));
        Player player = new Player();
        player.start();
        GameBoard.controllerInput.println(playerName.getText() + ";" + Integer.parseInt(playerColor.getValue().toString().substring(2, 8), 16));
    }
}
