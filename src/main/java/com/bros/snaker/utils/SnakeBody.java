package com.bros.snaker.utils;

import com.bros.snaker.data.SnakeBodyData;

public class SnakeBody {
    public static String getBody(int[] before, int[] present, int[] next) {
        if (before[0] != next[0] && before[1] != next[1]) {
            boolean isPresentAbove = present[0] < before[0];
            boolean isPresentBelow = present[0] > before[0];
            boolean isPresentLeft = present[1] < before[1];
            boolean isPresentRight = present[1] > before[1];

            boolean isNextAbove = next[0] < present[0];
            boolean isNextBelow = next[0] > present[0];
            boolean isNextLeft = next[1] < present[1];
            boolean isNextRight = next[1] > present[1];

            if (isPresentAbove && isNextLeft || isPresentRight && isNextBelow) {
                return SnakeBodyData.B_T_L;
            } else if (isPresentAbove && isNextRight || isPresentLeft && isNextBelow) {
                return SnakeBodyData.B_T_R;
            } else if (isPresentRight && isNextAbove || isPresentBelow && isNextLeft) {
                return SnakeBodyData.L_T_T;
            } else if (isPresentLeft && isNextAbove || isPresentBelow && isNextRight) {
                return SnakeBodyData.R_T_T;
            }
        }
        return SnakeBodyData.BODY;
    }

    public static String getTail(int[] before, int[] present) {
        if (before[0] < present[0]) {
            return SnakeBodyData.TAIL_UP;
        } else if (before[0] > present[0]) {
            return SnakeBodyData.TAIL_DOWN;
        } else if (before[1] < present[1]) {
            return SnakeBodyData.TAIL_LEFT;
        } else {
            return SnakeBodyData.TAIL_RIGHT;
        }
    }
}
