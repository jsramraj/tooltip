package com.ramaraj.tooltip;

import android.app.Activity;

import java.util.List;

public class ToolTipPresenter implements ToolTipListener.ToolTipOnDismissListener {
    private List<StaticTip> toolTips;
    private Activity activity;

    public ToolTipPresenter(ToolTipBuilder builder, Activity activity) {
        this.toolTips = builder.staticTipsForActivity(activity.getLocalClassName());
        this.activity = activity;
    }

    public void displayStaticTipsForActivity() {
        showNextTip();
    }

    private void showNextTip() {
        if (toolTips.size() > 0) {
            toolTips.get(0).setOnDismissListener(this);
            toolTips.get(0).displayTip(activity);
        }
    }

    @Override
    public void onTipDismissed(ToolTip tip) {
        if (toolTips.size() > 0) {
            toolTips.remove(0);
        }
        showNextTip();
    }
}
