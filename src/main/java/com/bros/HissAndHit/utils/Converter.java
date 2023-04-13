package com.bros.HissAndHit.utils;

import com.bros.HissAndHit.proto.Data;

public class Converter {
    public static int cantorPair(int x, int y) {
        return ((x + y) * (x + y + 1) / 2) + y;
    }

    public static int cantorPair(Data.Point data) {
        return cantorPair(data.getX(), data.getY());
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
