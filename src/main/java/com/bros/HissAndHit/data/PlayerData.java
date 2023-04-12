package com.bros.HissAndHit.data;

import com.bros.HissAndHit.proto.Data;

import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

public class PlayerData {
    public static Data.Array3D positions;
    public static Data.Food food;
    public static Data.PlayersMetaData metaData;
    public static Socket controllerSocket;
    public static Socket UISocket;
    public static String IPAddress;
    public static String roomCode;
    public static String roomIpAddress;
    public static int playerCount;
    public static String[] playerNames;
    public static String[] playerColors;
    public static CyclicBarrier ScoreBoardBarrier;
}
