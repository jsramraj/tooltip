package com.navram.tooltip.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.navram.tooltip.Constants;
import com.navram.tooltip.StaticTip;
import com.navram.tooltip.ToolTipModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        try {
            InputStream is = aContext.getAssets().open(aJsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Finding the available page related tooltip model values
     *
     * @param aToolTipData available tool tip data to be filtered
     * @param aPageName    page to be filtered
     * @return the page name related tooltip values
     */
    public static List<ToolTipModel> findToolTipModelItems(List<ToolTipModel> aToolTipData, @NonNull String aPageName) {

        List<ToolTipModel> pageToolTipModel = new ArrayList<>();

        if (StringUtils.isNullOrEmpty(aPageName) || aToolTipData == null || aToolTipData.isEmpty()) {
            return pageToolTipModel;
        }

        for (ToolTipModel toolTipModel : aToolTipData) {
            if (toolTipModel.getPageName().equals(aPageName)) {
                pageToolTipModel.add(toolTipModel);
            }
        }

        return pageToolTipModel;
    }

    /**
     * Finding the available page related tooltip model values
     *
     * @param aToolTipData available static tool tip data to be filtered
     * @param aPageName    page to be filtered
     * @return the page name related tooltip values
     */
    public static List<StaticTip> findStaticToolTipItems(List<StaticTip> aToolTipData, @NonNull String aPageName) {

        List<StaticTip> pageToolTipModel = new ArrayList<>();

        if (StringUtils.isNullOrEmpty(aPageName) || aToolTipData == null || aToolTipData.isEmpty()) {
            return pageToolTipModel;
        }

        for (StaticTip toolTipModel : aToolTipData) {
            if (toolTipModel.getActivityName().equals(aPageName)) {
                pageToolTipModel.add(toolTipModel);
            }
        }

        return pageToolTipModel;
    }
}

