package com.navram.tooltip;

import android.app.Activity;
import android.graphics.Color;

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

    private ToolTipListener.ToolTipOnDismissListener onDismissListener;

    ToolTipConfig config;
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

    protected ToolTipConfig toolTipConfig(Activity parentActivity) {
        if (config == null) {
            if (parentActivity instanceof ToolTipListener.ToolTipConfigChange) {
                config = ((ToolTipListener.ToolTipConfigChange) parentActivity).configForTip(this);
            }
        }
        if (config == null) {
            config = ToolTipConfig.getInstance();
        }
        return config;
    }

    protected int titleTextViewStyleId(Activity parentActivity) {
        ToolTipConfig config = toolTipConfig(parentActivity);
        if (config.getTipTitleStyleResId() > 0)
            return config.getTipTitleStyleResId();
        else
            return ToolTipConfig.getInstance().getTipTitleStyleResId();
    }

    protected int messageTextViewStyleId(Activity parentActivity) {
        ToolTipConfig config = toolTipConfig(parentActivity);
        if (config.getTipMessageStyleResId() > 0)
            return config.getTipMessageStyleResId();
        else
            return ToolTipConfig.getInstance().getTipMessageStyleResId();
    }

    protected int nextButtonStyleId(Activity parentActivity) {
        ToolTipConfig config = toolTipConfig(parentActivity);
        if (config.getNextButtonStyleResId() > 0)
            return config.getNextButtonStyleResId();
        else
            return ToolTipConfig.getInstance().getNextButtonStyleResId();
    }

    protected int overlayBackgroundColor(Activity parentActivity) {
        ToolTipConfig config = toolTipConfig(parentActivity);
        if (config.getOverlayBackgroundColor() != 0)
            return config.getOverlayBackgroundColor();
        else
            return ToolTipConfig.getInstance().getOverlayBackgroundColor();
    }
}
