package com.bros.HissAndHit.utils;

import com.bros.HissAndHit.data.SnakeBodyData;
import com.bros.HissAndHit.proto.Data;

public class SnakeBody {
    public static String getBody(Data.Point before, Data.Point present, Data.Point next) {
        if (before.getX() != next.getX() && before.getY() != next.getY()) {
            boolean isPresentAbove = present.getX() < before.getX();
            boolean isPresentBelow = present.getX() > before.getX();
            boolean isPresentLeft = present.getY() < before.getY();
            boolean isPresentRight = present.getY() > before.getY();

            boolean isNextAbove = next.getX() < present.getX();
            boolean isNextBelow = next.getX() > present.getX();
            boolean isNextLeft = next.getY() < present.getY();
            boolean isNextRight = next.getY() > present.getY();

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

    public static String getTail(Data.Point before, Data.Point present) {
        if (before.getX() < present.getX()) {
            return SnakeBodyData.TAIL_UP;
        } else if (before.getX() > present.getX()) {
            return SnakeBodyData.TAIL_DOWN;
        } else if (before.getY() < present.getY()) {
            return SnakeBodyData.TAIL_LEFT;
        } else {
            return SnakeBodyData.TAIL_RIGHT;
        }
    }

    public static String getHead(Data.Point before, Data.Point present) {
        if (before.getX() > present.getX()) {
            return SnakeBodyData.HEAD_TOP;
        } else if (before.getX() < present.getX()) {
            return SnakeBodyData.HEAD_BOTTOM;
        } else if (before.getY() > present.getY()) {
            return SnakeBodyData.HEAD_LEFT;
        } else {
            return SnakeBodyData.HEAD_RIGHT;
        }
    }
}
