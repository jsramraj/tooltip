package com.ramaraj.tooltip;

import android.app.Activity;

import java.util.List;

public class ToolTipPresenter {
    public static void displayStaticTipsForActivity(ToolTipBuilder builder, Activity activity) {
        List<StaticTip> toolTips = builder.staticTipsForActivity(activity.getLocalClassName());
        if (toolTips.size() > 0) {
            toolTips.get(0).displayTip(activity);
        }
    }
}
