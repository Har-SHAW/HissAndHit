package com.bros.snaker;

import com.bros.snaker.data.PlayerData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GameBoard extends Application {
    public static PrintWriter controllerInput;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameBoard.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (controllerInput != null) {
                controllerInput.println(event.getCode().toString());
            }
        });

        stage.setTitle("Snake");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address.isLinkLocalAddress() || address.isMulticastAddress()) {
                        continue;
                    }
                    if (address.getAddress().length == 4) {
                        PlayerData.IPAddress = address.getHostAddress();
                        String[] blocks = address.getHostAddress().split("\\.");
                        if (blocks.length != 4) {
                            throw new IllegalArgumentException("Invalid IPv4 address");
                        }
                        PlayerData.IPAddressPrefix = blocks[0] + "." + blocks[1] + "." + blocks[2];
                        PlayerData.roomCode = blocks[3];
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}