package com.bros.snaker.config;

import java.util.HashMap;
import java.util.Map;

public class Directions {
    public static Map<String, Integer> data = new HashMap<>() {{
        put("UP", 0);
        put("DOWN", 1);
        put("LEFT", 2);
        put("RIGHT", 3);
    }};
}
