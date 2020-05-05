package com.navram.tooltip;

import android.app.Activity;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.List;

/**
 * Used to present the tips to the user
 */
public class ToolTipPresenter implements ToolTipListener.ToolTipOnDismissListener, ViewTreeObserver.OnGlobalLayoutListener {
    private List<StaticTip> toolTips;
    private Activity activity;

    /**
     * Construct the ToolTipPresenter
     * @param builder The {@code ToolTipBuilder} object that has the array of the ToolTips
     * @param activity Activity that calls this method
     */
    public ToolTipPresenter(ToolTipBuilder builder, Activity activity) {
        this.toolTips = builder.staticTipsForActivity(activity.getLocalClassName());
        this.activity = activity;
    }

    /**
     * Starts displaying the static tips for the current activity one by one
     */
    public void displayStaticTipsForActivity() {
        showNextTip();
    }

    /**
     * Show the next available static tip
     */
    private void showNextTip() {
        for (ToolTip toolTip : toolTips) {
            boolean tipShown = toolTip.displayTip(activity);
            if (tipShown) {
                toolTip.setOnDismissListener(this);
                return;
            }

            // the tip could not be displayed,
            // possible reason is the target view is not visible or null
            // let's display the next tip
        }

        if (!toolTips.isEmpty()) {
            // few tips are still not displayed
            // this could be because, the target view is not a valid one or the target view is not yet added to the view
            // let's defer and observe for the target view to appear
            observeForDeferredTips();
        }
    }

    /**
     * If few of the target view is not yet displayed in the activity content view,
     * this method observers for the view to appear and show the tip eventually
     */
    private void observeForDeferredTips() {
        View activityView = activity.findViewById(android.R.id.content);
        activityView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * Removes the global layout listener
     */
    public void cleanUp () {
        View activityView = activity.findViewById(android.R.id.content);
        activityView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onTipDismissed(ToolTip tip) {
        if (!toolTips.isEmpty()) {
            toolTips.remove(tip);
        }
        showNextTip();
    }

    @Override
    public void onGlobalLayout() {
        // View has been re-drawn.
        // Check if any tips are present in the tipBuider
        showNextTip();
    }
}
