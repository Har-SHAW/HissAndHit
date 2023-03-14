package com.bros.snaker.utils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.stream.Collectors;

public class Converter {
    public static Deque<int[]>[] fromString(String data) {
        String[] dequeStrings = data.split(";");
        Deque<int[]>[] dequeArray = new ArrayDeque[dequeStrings.length];
        for (int i = 0; i < dequeStrings.length; i++) {
            String dequeString = dequeStrings[i].replace("[[", "").replace("]]", "");
            String[] intStrings = dequeString.split("],\\[");
            Deque<int[]> deque = new ArrayDeque<>();
            for (String intString : intStrings) {
                String[] ints = intString.split(",");
                int[] arr = new int[ints.length];
                arr[0] = Integer.parseInt(ints[0]);
                arr[1] = Integer.parseInt(ints[1]);
                deque.add(arr);
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
