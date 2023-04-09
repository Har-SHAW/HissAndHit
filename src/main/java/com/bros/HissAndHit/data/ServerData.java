package com.bros.HissAndHit.data;

import java.util.Deque;
import java.util.HashMap;
import java.util.concurrent.CyclicBarrier;

public class ServerData {
    public static Deque<int[]>[] positions;
    public static HashMap<Integer, Integer> hashMap;
    public static HashMap<Integer, int[]> foodMap;
    public static int[] directions;
    public static int playerCount;
    public static CyclicBarrier readyBarrier;
}
