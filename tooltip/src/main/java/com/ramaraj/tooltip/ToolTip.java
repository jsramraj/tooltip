package com.ramaraj.tooltip;

public class ToolTip {
    protected final int resourceId;

    public int getResourceId() {
        return resourceId;
    }

    public String getTipText() {
        return tipText;
    }

    public String getActivityName() {
        return activityName;
    }

    protected final String tipText;
    protected final String activityName;

    public ToolTip(String activityName, int resourceId, String tipText) {
        this.resourceId = resourceId;
        this.tipText = tipText;
        this.activityName = activityName;
    }
}
