package com.bros.snaker.utils;

import java.util.Arrays;
import java.util.Deque;
import java.util.stream.Collectors;

public class Converter {
    public static int[][][] fromString(String data) {
        String[] dequeStrings = data.split(";");
        int[][][] dequeArray = new int[dequeStrings.length][][];
        for (int i = 0; i < dequeStrings.length; i++) {
            String[] intStrings = dequeStrings[i]
                    .replace("[[", "")
                    .replace("]]", "")
                    .split("],\\[");

            int[][] deque = new int[intStrings.length][];

            for (int j = 0; j < intStrings.length; j++) {
                String[] ints = intStrings[j].split(",");
                deque[j] = Arrays.stream(ints).mapToInt(Integer::parseInt).toArray();
            }
            dequeArray[i] = deque;
        }
        return dequeArray;
    }

    public static String toString(Deque<int[]>[] dequeArray) {
        String separator = ";";
        return Arrays.stream(dequeArray)
                .map(deque -> Arrays.deepToString(deque.toArray()).replaceAll("\\s+", ""))
                .collect(Collectors.joining(separator));
    }
}
