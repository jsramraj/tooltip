package com.navram.tooltip.utils;

public class StringUtils {

    private StringUtils() {
        //Do nothing
    }

    public static boolean isNullOrEmpty(String str) {
        return (str == null || str.isEmpty());
    }
}
