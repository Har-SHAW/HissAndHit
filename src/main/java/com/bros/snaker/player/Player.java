package com.bros.snaker.player;

import com.bros.snaker.GameBoard;
import com.bros.snaker.config.Statics;
import com.bros.snaker.data.PlayerData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {

    public void start() throws IOException {
        PlayerData.UISocket = new Socket(PlayerData.IPAddressPrefix + "." + PlayerData.roomCode, Statics.PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(PlayerData.UISocket.getInputStream()));
        int controllerPort = Integer.parseInt(in.readLine());

        PlayerData.controllerSocket = new Socket(PlayerData.IPAddressPrefix + "." + PlayerData.roomCode, Statics.PORT + controllerPort);
        GameBoard.controllerInput = new PrintWriter(PlayerData.controllerSocket.getOutputStream(), true);

        Thread thread = new Thread(new UIThread(PlayerData.UISocket), "Player UI Thread");
        thread.start();
    }
}
