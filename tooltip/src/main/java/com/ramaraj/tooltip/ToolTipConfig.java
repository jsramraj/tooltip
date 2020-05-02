package com.ramaraj.tooltip;

public class ToolTipConfig {
    private static ToolTipConfig instance;
    private static final Object lock = new Object();

    public ToolTipConfig() {
    }

    public static ToolTipConfig getInstance() {

        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ToolTipConfig();
                }
            }
        }

        return instance;
    }

    private int styleResId;

    public int getStyleResId() {
        return styleResId;
    }

    public void setStyleResId(int styleResId) {
        this.styleResId = styleResId;
    }
}
