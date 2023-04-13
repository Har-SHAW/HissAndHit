package com.bros.HissAndHit;

import java.io.IOException;

public class IntroController {
    public void createRoom() throws IOException {
        GameBoard.setCreateRoom();
    }

    public void joinRoom() throws IOException {
        GameBoard.setJoinRoom();
    }
}
