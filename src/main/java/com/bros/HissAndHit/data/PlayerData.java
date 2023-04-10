package com.bros.HissAndHit.data;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class PlayerData {
    public static int[][][] positions;
    public static Socket controllerSocket;
    public static Socket UISocket;
    public static String IPAddress;
    public static String roomCode;
    public static String roomIpAddress;
    public static int playerCount;
    public static String[] playerNames;
    public static List<Integer> playerColors;
    public static CyclicBarrier ScoreBoardBarrier;
}
