package com.bros.HissAndHit.utils;

import java.net.*;
import java.util.Enumeration;

public class Network {
    private static boolean isWindows() {
        return (System.getProperty("os.name").toLowerCase().contains("win"));
    }

    private static boolean isLinux() {
        return (System.getProperty("os.name").toLowerCase().contains("nix") || System.getProperty("os.name").toLowerCase().contains("nux") || System.getProperty("os.name").toLowerCase().contains("aix"));
    }

    public static String getIpAddress() throws SocketException, UnknownHostException {
        if (isWindows()) {
            return getWindowsIpAddress();
        } else if (isLinux()) {
            return getLinuxIpAddress();
        } else {
            return null;
        }
    }

    private static String getWindowsIpAddress() throws UnknownHostException, SocketException {
        InetAddress localhost = InetAddress.getLocalHost();
        NetworkInterface wifiInterface = NetworkInterface.getByInetAddress(localhost);
        Enumeration<InetAddress> addresses = wifiInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
            InetAddress adder = addresses.nextElement();
            if (adder instanceof Inet4Address) {
                return adder.getHostAddress();
            }
        }
        return null;
    }

    private static String getLinuxIpAddress() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface face = interfaces.nextElement();
            if (face.isLoopback() || !face.isUp() || !face.getDisplayName().startsWith("w"))
                continue;

            Enumeration<InetAddress> addresses = face.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress adder = addresses.nextElement();
                if (adder instanceof Inet4Address) {
                    return adder.getHostAddress();
                }
            }
        }
        return null;
    }
}
