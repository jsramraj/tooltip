package com.ramaraj.tooltip.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ramaraj.tooltip.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResourceUtils {

    private ResourceUtils() {

    }

    public static int getResourceId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            Log.e(
                    Constants.TAG,
                    StringUtils.isNullOrEmpty(e.getLocalizedMessage()) ?
                            "Unknown" :
                            e.getLocalizedMessage()
            );
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

        if (stringIdentifiers == null || stringIdentifiers.isEmpty()) {
            return new int[0];
        }

        int[] identifiers = new int[stringIdentifiers.size()];

        for (int i = 0; i < stringIdentifiers.size(); i++) {
            identifiers[i] = getResourceId(activity, stringIdentifiers.get(i));
        }

        return identifiers;
    }

    public static String readJSONFromAsset(Context aContext, String aJsonFileName) {
        String json;
        try {
            InputStream is = aContext.getAssets().open(aJsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

