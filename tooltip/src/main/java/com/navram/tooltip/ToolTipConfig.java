package com.navram.tooltip;

import androidx.annotation.StyleRes;

/**
 * Configuration to customize the behaviour of the tip
 */
public class ToolTipConfig {
    private static ToolTipConfig instance;
    private static final Object lock = new Object();

    private int tipMessageStyleResId;
    private int tipTitleStyleResId;

    /**
     * Default constructor. Default styles for the tips are set here
     */
    public ToolTipConfig() {
        tipMessageStyleResId = R.style.defaultTipTextStyle;
        tipTitleStyleResId = R.style.defaultTipTitleStyle;
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

    public int getTipMessageStyleResId() {
        return tipMessageStyleResId;
    }

    /**
     * Customize the appearance of the tip message
     * @param tipMessageStyleResId Style resource if for the tip message
     */
    public void setTipMessageStyleResId(@StyleRes int tipMessageStyleResId) {
        this.tipMessageStyleResId = tipMessageStyleResId;
    }

    public int getTipTitleStyleResId() {
        return tipTitleStyleResId;
    }

    /**
     * Customize the appearance of the tip message
     * @param tipTitleStyleResId Style resource id for the tip title
     */
    public void setTipTitleStyleResId(@StyleRes int tipTitleStyleResId) {
        this.tipTitleStyleResId = tipTitleStyleResId;
    }
}
