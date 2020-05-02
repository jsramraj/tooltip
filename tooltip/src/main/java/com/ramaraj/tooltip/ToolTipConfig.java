package com.ramaraj.tooltip;

public class ToolTipConfig {
    private static ToolTipConfig instance;
    private static final Object lock = new Object();

    public ToolTipConfig() {
        tipTextStyleResId = R.style.defaultTipTextStyle;
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

    public static void setInstance(ToolTipConfig instance) {
        ToolTipConfig.instance = instance;
    }

    private int tipTextStyleResId;

    public int getTipTextStyleResId() {
        return tipTextStyleResId;
    }

    public void setTipTextStyleResId(int tipTextStyleResId) {
        this.tipTextStyleResId = tipTextStyleResId;
    }
}
