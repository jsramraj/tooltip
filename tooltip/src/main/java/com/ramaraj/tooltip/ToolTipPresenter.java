package com.ramaraj.tooltip;

import android.app.Activity;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.List;

public class ToolTipPresenter implements ToolTipListener.ToolTipOnDismissListener, ViewTreeObserver.OnGlobalLayoutListener {
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
        for (ToolTip toolTip : toolTips) {
            boolean tipShown = toolTip.displayTip(activity);
            if (tipShown) {
                toolTip.setOnDismissListener(this);
                return;
            } else {
                // the tip could not be displayed,
                // possible reason is the target view is not visible or null
                // let's display the next tip
                continue;
            }
        }

        if (toolTips.size() > 0) {
            // few tips are still not displayed
            // this could be because, the target view is not a valid one or the target view is not yet added to the view
            // let's defer and observe for the target view to appear
            observeForDeferredTips();
        }
    }

    private void observeForDeferredTips() {
        View activityView = activity.findViewById(android.R.id.content);
        activityView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public void cleanUp () {
        View activityView = activity.findViewById(android.R.id.content);
        activityView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onTipDismissed(ToolTip tip) {
        if (toolTips.size() > 0) {
            toolTips.remove(tip);
        }
        showNextTip();
    }

    @Override
    public void onGlobalLayout() {
        showNextTip();
    }
}
