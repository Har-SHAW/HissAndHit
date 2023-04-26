package com.bros.HissAndHit.config;

import java.util.HashMap;
import java.util.Map;

public class Directions {
    public static Map<String, Integer> directions = new HashMap<>() {{
        put("W", 0);
        put("A", 1);
        put("S", 2);
        put("D", 3);
        put("UP", 0);
        put("LEFT", 1);
        put("DOWN", 2);
        put("RIGHT", 3);
    }};
    public static Map<String, Integer> speed = new HashMap<>() {{
        put("S_P", 1);
        put("S_R", 0);
        put("L_P", -1);
        put("L_R", 0);
    }};
}
