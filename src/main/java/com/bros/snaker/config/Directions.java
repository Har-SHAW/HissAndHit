package com.bros.snaker.config;

import java.util.HashMap;
import java.util.Map;

public class Directions {
    public static Map<String, Integer> data = new HashMap<>() {{
        put("W", 0);
        put("S", 1);
        put("A", 2);
        put("D", 3);
    }};
}
