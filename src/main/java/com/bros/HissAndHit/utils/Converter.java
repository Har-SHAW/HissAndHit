package com.bros.HissAndHit.utils;

import com.bros.HissAndHit.proto.Data;

import java.util.Arrays;
import java.util.Deque;

public class Converter {
    public static int[][][] fromString(String data) {
        String[] dequeStrings = data.split(";");
        int[][][] dequeArray = new int[dequeStrings.length][][];
        for (int i = 0; i < dequeStrings.length; i++) {
            if (dequeStrings[i].length() == 2) {
                dequeArray[i] = new int[0][0];
                continue;
            }
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

    public static int cantorPair(Data.Array1D data) {
        return ((data.getX() + data.getY()) * (data.getX() + data.getY() + 1) / 2) + data.getY();
    }

    public static int ipv4ToInt(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        int result = 0;
        for (String octet : octets) {
            result = result * 256 + Integer.parseInt(octet);
        }
        return result;
    }

    public static String intToIpv4(int ipAddress) {
        int d = ipAddress % 256;
        int c = (ipAddress / 256) % 256;
        int b = (ipAddress / 65536) % 256;
        int a = (ipAddress / 16777216) % 256;
        return a + "." + b + "." + c + "." + d;
    }
}
