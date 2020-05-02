package com.ramaraj.tooltip.utils;

import android.app.Activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ResourceUtils {

    public static int getResourceId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getResourceId(Activity activity, String resName) {
        try {
            return Integer.parseInt(resName);
        } catch (NumberFormatException e) {
            return activity.getResources().getIdentifier(resName, "id", activity.getPackageName());
        }
    }

    public static int[] getResourceIdentifiers(Activity activity, String[] stringIdentifiers) {
        if (stringIdentifiers == null || stringIdentifiers.length == 0)
            return null;
        int[] identifiers = new int[stringIdentifiers.length];

        for (int i = 0; i < stringIdentifiers.length; i++) {
            identifiers[i] = getResourceId(activity, stringIdentifiers[i]);
        }

        return identifiers;
    }
}

