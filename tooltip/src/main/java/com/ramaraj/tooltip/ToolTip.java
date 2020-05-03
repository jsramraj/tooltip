package com.ramaraj.tooltip;

import android.app.Activity;

public abstract class ToolTip {
    protected final int resourceId;
    protected final String tipTitle;
    protected final String tipText;
    protected final String activityName;

    protected ToolTipListener.ToolTipOnDismissListener onDismissListener;

    public ToolTip(String activityName, int resourceId, String tipTitle, String tipText) {
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

    /**
     * @param activity to display the tooltip view
     */
    void displayTip(Activity activity) {

    }

    /**
     * dismissing the tooltip view
     */
    void dismissTip() {
        if (onDismissListener != null) {
            onDismissListener.onTipDismissed(this);
        }
    }

    public void setOnDismissListener(ToolTipListener.ToolTipOnDismissListener listener) {
        this.onDismissListener = listener;
    }
}
