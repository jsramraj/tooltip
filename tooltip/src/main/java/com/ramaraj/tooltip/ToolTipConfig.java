package com.ramaraj.tooltip;

import androidx.annotation.StyleRes;

/**
 * Configuration to customize the behaviour of the tip
 */
public class ToolTipConfig {
    private static ToolTipConfig instance;
    private static final Object lock = new Object();

    private int tipTextStyleResId;
    private int tipTitleTextStyleResId;

    /**
     * Default constructor. Default styles for the tips are set here
     */
    public ToolTipConfig() {
        tipTextStyleResId = R.style.defaultTipTextStyle;
        tipTitleTextStyleResId = R.style.defaultTipTitleStyle;
    }

    /**
     * Create and returns the Singleton object
     * @return Global instance object
     */
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

    /**
     * Set the singleton object
     * @param instance Singleton instance
     */
    public static void setInstance(ToolTipConfig instance) {
        ToolTipConfig.instance = instance;
    }

    public int getTipTextStyleResId() {
        return tipTextStyleResId;
    }

    /**
     * Customize the appearance of the tip message
     * @param tipTextStyleResId Style resource if for the tip message
     */
    public void setTipTextStyleResId(@StyleRes int tipTextStyleResId) {
        this.tipTextStyleResId = tipTextStyleResId;
    }

    public int getTipTitleTextStyleResId() {
        return tipTitleTextStyleResId;
    }

    /**
     * Customize the appearance of the tip message
     * @param tipTitleTextStyleResId Style resource id for the tip title
     */
    public void setTipTitleTextStyleResId(@StyleRes int tipTitleTextStyleResId) {
        this.tipTitleTextStyleResId = tipTitleTextStyleResId;
    }
}
