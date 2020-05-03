package com.ramaraj.tooltip;

import android.app.Activity;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;

public abstract class ToolTip {
    protected final int resourceId;
    protected final String tipTitle;
    protected final String tipText;
    protected final String activityName;

    protected ToolTipListener.ToolTipOnDismissListener onDismissListener;

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

    /**
     * @param activity to display the tooltip view
     */
    @CallSuper
    void displayTip(Activity activity) {
        if (activity instanceof ToolTipListener.ToolTipOnShowListener) {
            ((ToolTipListener.ToolTipOnShowListener) activity).onTipShown(this);
        }
    }

    /**
     * dismissing the tooltip view
     */
    @CallSuper
    void dismissTip() {
        if (onDismissListener != null) {
            onDismissListener.onTipDismissed(this);
        }
    }

    public void setOnDismissListener(ToolTipListener.ToolTipOnDismissListener listener) {
        this.onDismissListener = listener;
    }
}
