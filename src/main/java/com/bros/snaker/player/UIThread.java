package com.bros.snaker.player;

import com.bros.snaker.HelloController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Deque;

public class UIThread implements Runnable {
    Socket UISocket;

    UIThread(Socket socket) {
        this.UISocket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = UISocket.getInputStream();

            while (true) {
                byte[] buffer = new byte[102400];
                int bytesRead = inputStream.read(buffer);
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
                Player.positions = (Deque<int[]>[]) objectInputStream.readObject();
                HelloController.update();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Data Loss");
        }

    }
}
