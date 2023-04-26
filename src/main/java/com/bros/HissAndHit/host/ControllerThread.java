package com.bros.HissAndHit.host;

import com.bros.HissAndHit.config.Directions;
import com.bros.HissAndHit.config.Statics;
import com.bros.HissAndHit.data.ServerData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;


public class ControllerThread implements Runnable {
    int playerId;
    int port;
    ServerSocket serverSocket;
    Socket client;

    ControllerThread(int i) {
        this.playerId = i;
        this.port = Statics.PORT + i + 1;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            client = serverSocket.accept();
            serverSocket.close();
            System.out.println(playerId + 1 + " controller connected");
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String[] strArr = in.readLine().split(";");
            ServerData.playerNames[playerId] = strArr[0];
            ServerData.playerColors[playerId] = strArr[1];

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

            String str;
            while ((str = in.readLine()) != null) {
                Integer data;
                if ((data = Directions.directions.get(str)) != null && (data & 1) != (ServerData.directions[playerId] & 1)) {
                    ServerData.directions[playerId] = data;
                } else if ((data = Directions.speed.get(str)) != null) {
                    ServerData.speed[playerId] = data;
                }
            }
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //ignore
        }
    }
}
