package com.bros.HissAndHit.config;

import java.util.HashMap;
import java.util.Map;

public class Directions {
    public static Map<String, Integer> data = new HashMap<>() {{
        put("W", 0);
        put("S", 1);
        put("A", 2);
        put("D", 3);
        put("UP", 0);
        put("DOWN", 1);
        put("LEFT", 2);
        put("RIGHT", 3);
    }};
}
