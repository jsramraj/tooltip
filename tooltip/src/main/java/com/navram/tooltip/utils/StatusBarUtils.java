package com.navram.tooltip.utils;

import android.content.Context;
import android.content.res.Resources;

public class StatusBarUtils {

    private StatusBarUtils() {
        //Do nothing
    }

    public static int getStatusBarOffset(Context context) {
        int result = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }

        return result;
    }
}
