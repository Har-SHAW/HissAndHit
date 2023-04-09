package com.bros.HissAndHit;

import javafx.event.ActionEvent;

import java.io.IOException;

public class IntroController {
    public void createRoom(ActionEvent actionEvent) throws IOException {
        GameBoard.setCreateRoom();
    }

    public void joinRoom(ActionEvent actionEvent) throws IOException {
        GameBoard.setJoinRoom();
    }
}
