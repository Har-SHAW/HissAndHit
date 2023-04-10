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
    Socket client;
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
            client = serverSocket.accept();
            serverSocket.close();
            System.out.println(playerId + 1 + " controller connected");
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            ServerData.playerNames[playerId] = in.readLine();
            metaData.get(playerId)[MetaIndexes.COLOR] = Integer.parseInt(in.readLine());

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
                Integer data = Directions.data.get(str);
                if (data != null) {
                    ServerData.directions[playerId] = data;
                } else if (str.equals("S_P")) {
                    ServerData.speed[playerId] = true;
                } else if (str.equals("S_R")) {
                    ServerData.speed[playerId] = false;
                }
            }
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //ignore
        }
    }
}
