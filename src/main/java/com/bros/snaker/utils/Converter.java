package com.bros.snaker.utils;

import java.util.Arrays;
import java.util.Deque;

public class Converter {
    public static int[][][] fromString(String data) {
        String[] dequeStrings = data.split(";");
        int[][][] dequeArray = new int[dequeStrings.length][][];
        for (int i = 0; i < dequeStrings.length; i++) {
            String intString = dequeStrings[i].substring(2, dequeStrings[i].length() - 2);
            String[] intStrings = intString.split("],\\[");
            int[][] deque = new int[intStrings.length][];
            for (int j = 0; j < intStrings.length; j++) {
                String[] ints = intStrings[j].split(",");
                deque[j] = new int[ints.length];
                for (int k = 0; k < ints.length; k++) {
                    deque[j][k] = Integer.parseInt(ints[k]);
                }
            }
            dequeArray[i] = deque;
        }
        return dequeArray;
    }

    public static String toString(Deque<int[]>[] dequeArray) {
        StringBuilder sb = new StringBuilder();
        String separator = ";";
        for (Deque<int[]> deque : dequeArray) {
            sb.append(Arrays.deepToString(deque.toArray()).replaceAll("\\s+", ""));
            sb.append(separator);
        }
        return sb.toString();
    }

    public static int cantorPair(int x, int y) {
        return ((x + y) * (x + y + 1) / 2) + y;
    }

    public static int cantorPair(int x, int y, int z) {
        int pairXY = cantorPair(x, y);
        return cantorPair(pairXY, z);
    }
}
