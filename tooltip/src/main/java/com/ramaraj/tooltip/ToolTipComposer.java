package com.ramaraj.tooltip;

import java.util.HashMap;

/**
 * Acts as an data source for the tooltip
 * Primary entry point for the tool tip library
 * This class stores the necessary information to construct the tip
 */
public class ToolTipComposer {

    private HashMap<String, String[]> tipIdentifiers;
    private HashMap<String, String[]> tipTitles;
    private HashMap<String, String[]> tipsText;

    /**
     * Default constructor
     * Stores the resource id, title and text of the tip
     * @param tipIdentifiers Array of resource id
     * @param tipTitles Array of titles for the tip
     * @param tipsText Array of the tip message
     * @param globalConfig App wide configuration for constructing the tips
     */
    private ToolTipComposer(HashMap<String, String[]> tipIdentifiers,
                            HashMap<String, String[]> tipTitles,
                            HashMap<String, String[]> tipsText,
                            ToolTipConfig globalConfig) {
        this.tipIdentifiers = tipIdentifiers;
        this.tipTitles = tipTitles;
        this.tipsText = tipsText;
        ToolTipConfig.setInstance(globalConfig);
    }

    public String[] tipIdentifiers(String activityName) {
        return tipIdentifiers.get(activityName);
    }

    public String[] tipTitles(String activityName) {
        return tipTitles.get(activityName);
    }

    public String[] tipTexts(String activityName) {
        return tipsText.get(activityName);
    }

    /**
     * Helper class to create the TipComposer object
     */
    public static class Builder {
        private HashMap<String, String[]> tipIdentifiers;
        private HashMap<String, String[]> tipTitles;
        private HashMap<String, String[]> tipsText;
        private ToolTipConfig globalConfig;

        /**
         * Default constructor
         */
        public Builder() {
            this.tipIdentifiers = new HashMap<>();
            this.tipTitles = new HashMap<>();
            this.tipsText = new HashMap<>();
        }

        /**
         * Add static tip data
         * @param activityName Local class name of the activity
         * @param identifiers Array of resource id
         * @param titles Array of titles for the tip
         * @param tips Array of the tip message
         * @return A {@code ToolTipComposer.Builder} object
         */
        public Builder addStaticTips(String activityName, String[] identifiers, String[] titles, String[] tips) {
            tipIdentifiers.put(activityName, identifiers);
            tipTitles.put(activityName, titles);
            tipsText.put(activityName, tips);
            return this;
        }

        /**
         * Set the app wide configuration to customize the appearance of the tip
         * @param globalConfig
         * @return A {@code ToolTipComposer.Builder} object
         */
        public Builder setGlobalConfig(ToolTipConfig globalConfig) {
            this.globalConfig = globalConfig;
            return this;
        }

        /**
         * Create the tooltip composer object from all the tip data and info we have
         * @return A new instance of {@code ToolTipComposer} class
         */
        public ToolTipComposer build() {
            return new ToolTipComposer(tipIdentifiers, tipTitles, tipsText, globalConfig);
        }
    }
}
