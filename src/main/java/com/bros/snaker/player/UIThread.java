package com.bros.snaker.player;

import com.bros.snaker.HelloController;
import javafx.util.Pair;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Deque;

@RequiredArgsConstructor
public class UIThread implements Runnable {
    @NonNull
    Socket UISocket;

    @Override
    public void run() {
        try {
            InputStream inputStream = UISocket.getInputStream();
            while (true) {
                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
                Player.positions = (Deque<int[]>[]) objectInputStream.readObject();
                HelloController.update();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
