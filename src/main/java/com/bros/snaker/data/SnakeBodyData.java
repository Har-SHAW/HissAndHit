package com.bros.snaker.data;

import com.bros.snaker.config.Statics;

public class SnakeBodyData {
    public static String B_T_R = "-fx-background-radius: " + (Statics.SNAKE_CELL_HEIGHT - 8) + " 0 0 0 ;";
    public static String B_T_L = "-fx-background-radius: 0 " + (Statics.SNAKE_CELL_HEIGHT - 8) + " 0 0;";
    public static String L_T_T = "-fx-background-radius: 0 0 " + (Statics.SNAKE_CELL_HEIGHT - 8) + " 0;";
    public static String R_T_T = "-fx-background-radius: 0 0 0 " + (Statics.SNAKE_CELL_HEIGHT - 8) + ";";
    public static String HEAD_TOP = "-fx-background-radius: " + Statics.SNAKE_CELL_HEIGHT + " " + Statics.SNAKE_CELL_HEIGHT + " 0 0;";
    public static String HEAD_RIGHT = "-fx-background-radius: 0 " + Statics.SNAKE_CELL_HEIGHT + " " + Statics.SNAKE_CELL_HEIGHT + " 0;";
    public static String HEAD_BOTTOM = "-fx-background-radius: 0 0 " + Statics.SNAKE_CELL_HEIGHT + " " + Statics.SNAKE_CELL_HEIGHT + ";";
    public static String HEAD_LEFT = "-fx-background-radius: " + Statics.SNAKE_CELL_HEIGHT + " 0 0 " + Statics.SNAKE_CELL_HEIGHT + ";";
    public static String BODY = "";
    public static String TAIL_UP = "-fx-shape: \"M 0 0 L 50 50 L 100 0 Z\";";
    public static String TAIL_DOWN = "-fx-shape: \"M 0 50 L 50 0 L 100 50 Z\";";
    public static String TAIL_RIGHT = "-fx-shape: \"M 50 0 L 0 25 L 50 50 Z\";";
    public static String TAIL_LEFT = "-fx-shape: \"M 0 0 L 50 25 L 0 50 Z\";";
}
