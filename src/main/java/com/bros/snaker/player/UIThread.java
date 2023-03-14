package com.bros.snaker.player;

import com.bros.snaker.HelloController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;

public class UIThread implements Runnable {
    Socket UISocket;

    UIThread(Socket socket) {
        this.UISocket = socket;
    }

    Deque<int[]>[] fromString(String data) {
        String[] dequeStrings = data.split(";");
        Deque<int[]>[] dequeArray = new ArrayDeque[dequeStrings.length];
        for (int i = 0; i < dequeStrings.length; i++) {
            String dequeString = dequeStrings[i].replace("[[", "").replace("]]", "");
            String[] intStrings = dequeString.split("],\\[");
            Deque<int[]> deque = new ArrayDeque<>();
            for (String intString : intStrings) {
                String[] ints = intString.split(",");
                int[] arr = new int[ints.length];
                arr[0] = Integer.parseInt(ints[0]);
                arr[1] = Integer.parseInt(ints[1]);
                deque.add(arr);
            }
            dequeArray[i] = deque;
        }
        return dequeArray;
    }

    @Override
    public void run() {
        int bytesRead = 0;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(UISocket.getInputStream()));
            while (true) {
                String message = reader.readLine();
                Player.positions = fromString(message);
                HelloController.update();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Data Loss");
            System.out.println(bytesRead);
        }

    }
}
