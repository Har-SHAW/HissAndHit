package com.bros.HissAndHit.host;

import com.bros.HissAndHit.config.Directions;
import com.bros.HissAndHit.config.MetaIndexes;
import com.bros.HissAndHit.config.Statics;
import com.bros.HissAndHit.data.ServerData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;


public class ControllerThread implements Runnable {
    int playerId;
    int port;
    ServerSocket serverSocket;
    List<int[]> metaData;

    ControllerThread(int i) {
        this.playerId = i;
        this.port = Statics.PORT + i + 1;
        this.metaData = (List<int[]>) ServerData.positions[ServerData.playerCount + 1];
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            System.out.println(playerId + 1 + " controller connected");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                String input = in.readLine();
                if (input.startsWith("NAME")) {
                    ServerData.playerNames[playerId] = input.split(":")[1];
                    break;
                }
            }

            while (true) {
                String input = in.readLine();
                if (input.startsWith("COLOR")) {
                    metaData.get(playerId)[MetaIndexes.COLOR] = Integer.parseInt(input.split(":")[1]);
                    break;
                }
            }

            while (true) {
                if (in.readLine().equals("LOAD")) {
                    ServerData.loadBarrier.await();
                    break;
                }
            }

            while (true) {
                if (in.readLine().equals("READY")) {
                    ServerData.readyBarrier.await();
                    break;
                }
            }

            while (true) {
                Integer data = Directions.data.get(in.readLine());
                if (data != null) {
                    ServerData.directions[playerId] = data;
                }
            }

        } catch (IOException | BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
