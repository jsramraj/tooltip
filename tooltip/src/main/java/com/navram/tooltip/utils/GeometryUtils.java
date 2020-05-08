package com.navram.tooltip.utils;

public class GeometryUtils {
    public static boolean isAlmostSquare(int width, int height, float deviation) {
        float hwRatio;
        if (width > height) {
            hwRatio = width / height;
        } else {
            hwRatio = height / width;
        }
        if (hwRatio < 1) {
            return (1 - hwRatio < deviation);
        } else {
            return (hwRatio - 1) < deviation;
        }
    }
}
