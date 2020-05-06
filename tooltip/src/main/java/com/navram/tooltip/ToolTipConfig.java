package com.navram.tooltip;

import androidx.annotation.StyleRes;

/**
 * Configuration to customize the behaviour of the tip
 */
public class ToolTipConfig {

    private static final Object LOCK = new Object();

    private static volatile ToolTipConfig instance;

    private int tipMessageStyleResId;
    private int tipTitleStyleResId;
    private int nextButtonStyleResId;

    /**
     * Default constructor. Default styles for the tips are set here
     */
    public ToolTipConfig() {
        tipMessageStyleResId = R.style.defaultTipTextStyle;
        tipTitleStyleResId = R.style.defaultTipTitleStyle;
        nextButtonStyleResId = R.style.defaultNextButtonStyle;
    }

    /**
     * Create and returns the Singleton object
     * @return Global instance object
     */
    public static ToolTipConfig getInstance() {

        ToolTipConfig localResource = instance;

        if (localResource == null) {
            synchronized (LOCK) {
                localResource = instance;
                if (localResource == null) {
                    instance = localResource = new ToolTipConfig();
                }
            }
        }

        return localResource;
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

    public int getNextButtonStyleResId() {
        return nextButtonStyleResId;
    }

    public void setNextButtonStyleResId(int nextButtonStyleResId) {
        this.nextButtonStyleResId = nextButtonStyleResId;
    }
}
