package com.bros.HissAndHit.data;

import com.bros.HissAndHit.proto.Data;

import java.util.Deque;
import java.util.HashMap;
import java.util.concurrent.CyclicBarrier;

public class ServerData {
    public static Deque<int[]>[] positions;
    public static Data.PlayersMetaData metaData;
    public static HashMap<Integer, Integer> hashMap;
    public static HashMap<Integer, Data.Point> foodMap;
    public static int[] directions;
    public static int[] speed;
    public static int playerCount;
    public static CyclicBarrier readyBarrier;
    public static CyclicBarrier loadBarrier;
    public static String[] playerNames;
    public static String[] playerColors;
}
