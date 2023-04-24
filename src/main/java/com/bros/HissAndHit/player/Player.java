package com.bros.HissAndHit.player;

import com.bros.HissAndHit.GameBoard;
import com.bros.HissAndHit.config.Statics;
import com.bros.HissAndHit.data.PlayerData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

public class Player {

    public void start(String name, String color) throws IOException {
        PlayerData.UISocket = new Socket(PlayerData.roomIpAddress, Statics.PORT);
        PlayerData.ScoreBoardBarrier = new CyclicBarrier(2);
        BufferedReader in = new BufferedReader(new InputStreamReader(PlayerData.UISocket.getInputStream()));
        int controllerPort = Integer.parseInt(in.readLine());

        PlayerData.controllerSocket = new Socket(PlayerData.roomIpAddress, Statics.PORT + controllerPort);
        GameBoard.controllerInput = new PrintWriter(PlayerData.controllerSocket.getOutputStream(), true);

        GameBoard.controllerInput.println(name + ";" + color.replaceAll("0x", "#"));

        Thread thread = new Thread(new UIThread(PlayerData.UISocket), "Player UI Thread");
        thread.start();
    }
}
