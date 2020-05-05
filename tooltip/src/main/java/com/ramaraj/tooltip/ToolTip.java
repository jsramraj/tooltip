package com.ramaraj.tooltip;

import android.app.Activity;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;

/**
 * Base class for the ToolTip
 */
public abstract class ToolTip {

    private final int resourceId;
    private final String tipTitle;
    private final String tipText;
    private final String activityName;

    protected ToolTipListener.ToolTipOnDismissListener onDismissListener;

    /**
     * Default constructor to create the ToolTip
     *
     * @param activityName Local class name of the activity (Can get by calling the getLocalClassName()
     *                     from the activity instance)
     * @param resourceId   Resource id of the view for which the user has acknowledged the tip
     * @param tipTitle     Optional title of the tip to display over the tip description
     * @param tipText      Actual tip message (or hint/tip) to be displayed for the view
     */
    public ToolTip(String activityName, @IdRes int resourceId, String tipTitle, String tipText) {
        this.resourceId = resourceId;
        this.tipTitle = tipTitle;
        this.tipText = tipText;
        this.activityName = activityName;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getTipText() {
        return tipText;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getTipTitle() {
        return tipTitle;
    }

    /**
     * Display the tip
     *
     * @param activity to display the tooltip view
     */
    @CallSuper
    boolean displayTip(Activity activity) {
        if (activity instanceof ToolTipListener.ToolTipOnShowListener) {
            ((ToolTipListener.ToolTipOnShowListener) activity).onTipShown(this);
        }
        return true;
    }

    /**
     * dismiss the current tooltip, makes a call to the onTipDismissed and marks the tip as acknowledged
     */
    @CallSuper
    void dismissTip() {
        if (onDismissListener != null) {
            onDismissListener.onTipDismissed(this);
        }
        PersistentManager.getInstance().acknowledge(activityName, resourceId);
    }

    /**
     * When a listener is passed, the onTipDismissed method of this listener gets called when the
     * user presses the next/close button
     *
     * @param listener On Dismiss listener
     */
    public void setOnDismissListener(ToolTipListener.ToolTipOnDismissListener listener) {
        this.onDismissListener = listener;
    }
}
