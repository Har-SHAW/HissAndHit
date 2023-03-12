package com.bros.snaker.player;

import com.bros.snaker.HelloController;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@RequiredArgsConstructor
public class UIThread implements Runnable {
    @NonNull
    Socket UISocket;

    public static List<Deque<Pair<Integer, Integer>>> stringToList(String str) {
        List<Deque<Pair<Integer, Integer>>> list = new ArrayList<>();
        str = str.substring(1, str.length() - 1);
        String[] dequeStrs = str.split("],\\[");
        for (String dequeStr : dequeStrs) {
            Deque<Pair<Integer, Integer>> deque = new ArrayDeque<>();
            dequeStr = dequeStr.substring(1, dequeStr.length() - 1);
            String[] pairStrs = dequeStr.split(",");
            for (int i = 0; i < pairStrs.length; i += 2) {
                int key = Integer.parseInt(pairStrs[i].substring(1));
                int value = Integer.parseInt(pairStrs[i + 1].substring(0, pairStrs[i + 1].length() - 1));
                deque.add(new Pair<>(key, value));
            }
            list.add(deque);
        }
        return list;
    }


    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(UISocket.getInputStream()));
            while (true) {
                String response = in.readLine();
                Player.positions = stringToList(response);
                HelloController.update();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
