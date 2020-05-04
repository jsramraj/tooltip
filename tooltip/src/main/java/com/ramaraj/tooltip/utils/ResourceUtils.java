package com.ramaraj.tooltip.utils;

import android.app.Activity;

import java.lang.reflect.Field;
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

    public static int[] getResourceIdentifiers(Activity activity, List<String> stringIdentifiers) {
        if (stringIdentifiers == null || stringIdentifiers.size() == 0)
            return null;
        int[] identifiers = new int[stringIdentifiers.size()];

        for (int i = 0; i < stringIdentifiers.size(); i++) {
            identifiers[i] = getResourceId(activity, stringIdentifiers.get(i));
        }

        return identifiers;
    }
}

