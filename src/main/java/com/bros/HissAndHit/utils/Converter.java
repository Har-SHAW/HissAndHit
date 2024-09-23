package com.bros.HissAndHit.utils;

import com.bros.HissAndHit.proto.Data;

public class Converter {
    public static int cantorPair(int x, int y) {
        return ((x + y) * (x + y + 1) / 2) + y;
    }

    public static int cantorPair(Data.Point data) {
        return cantorPair(data.getX(), data.getY());
    }

    public static long ipv4ToLong(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        long result = 0;
        for (String octet : octets) {
            result = result * 256 + Integer.parseInt(octet);
        }
        return result;
    }

    public static String longToIpv4(long ipAddress) {
        long d = ipAddress % 256;
        long c = (ipAddress / 256) % 256;
        long b = (ipAddress / 65536) % 256;
        long a = (ipAddress / 16777216) % 256;
        return a + "." + b + "." + c + "." + d;
    }
}
