package com.ramaraj.tooltip;

import androidx.annotation.StyleRes;

public class ToolTipConfig {
    private static ToolTipConfig instance;
    private static final Object lock = new Object();

    private int tipTextStyleResId;
    private int tipTitleTextStyleResId;

    public ToolTipConfig() {
        tipTextStyleResId = R.style.defaultTipTextStyle;
        tipTitleTextStyleResId = R.style.defaultTipTitleStyle;
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

    public int getTipTextStyleResId() {
        return tipTextStyleResId;
    }

    public void setTipTextStyleResId(@StyleRes int tipTextStyleResId) {
        this.tipTextStyleResId = tipTextStyleResId;
    }

    public int getTipTitleTextStyleResId() {
        return tipTitleTextStyleResId;
    }

    public void setTipTitleTextStyleResId(@StyleRes int tipTitleTextStyleResId) {
        this.tipTitleTextStyleResId = tipTitleTextStyleResId;
    }
}
