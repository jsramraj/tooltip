package com.ramaraj.tooltip;

public class ToolTip {
    protected final int resourceId;
    protected final String tipTitle;
    protected final String tipText;
    protected final String activityName;

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
}
