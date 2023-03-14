package com.bros.snaker.data;

import java.net.Socket;
import java.util.Deque;

public class PlayerData {
    public static Deque<int[]>[] positions;
    public static Socket controllerSocket;
    public static Socket UISocket;
    public static String IPAddress;
    public static String IPAddressPrefix;
    public static String roomCode;
}
