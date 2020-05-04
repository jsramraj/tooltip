package com.ramaraj.tooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ramaraj.tooltip.Constants.*;

/**
 * Acts as an data source for the tooltip
 * Primary entry point for the tool tip library
 * This class stores the necessary information to construct the tip
 */
public class ToolTipComposer {

    private final HashMap<String, List<HashMap<String, String>>> allTipData;

    /**
     * Default constructor
     * Stores the resource id, title and text of the tip
     * @param allTipData All the trip data with key being the activity name
     * @param globalConfig App wide configuration for constructing the tips
     */
    private ToolTipComposer(HashMap<String, List<HashMap<String, String>>> allTipData,
                            ToolTipConfig globalConfig) {
        this.allTipData = allTipData;
        ToolTipConfig.setInstance(globalConfig);
    }

    public HashMap<String, List<HashMap<String, String>>> getAllTipData() {
        return allTipData;
    }

    /**
     * Helper class to create the TipComposer object
     */
    public static class Builder {
        private HashMap<String, List<HashMap<String, String>>> allTipData;
        private ToolTipConfig globalConfig;

        /**
         * Default constructor
         */
        public Builder() {
            allTipData = new HashMap<>();
        }

        /**
         * Add static tip data
         * @param activityName Local class name of the activity
         * @param identifiers Array of resource id
         * @param titles Array of titles for the tip
         * @param messages Array of the tip message
         * @return A {@code ToolTipComposer.Builder} object
         */
        public Builder addStaticTips(String activityName, String[] identifiers, String[] titles, String[] messages) {
            for (int i = 0; i < identifiers.length; i++) {
                this.addStaticTip(activityName, identifiers[i], titles[i], messages[i]);
            }
            return this;
        }

        public Builder addStaticTip(String activityName, String resourceId, String title, String message) {
            HashMap<String, String> tip = new HashMap<>();
            tip.put(RESOURCE_ID_KEY, resourceId);
            tip.put(TIP_TITLE_ID_KEY, title);
            tip.put(TIP_MESSAGE_ID_KEY, message);

            List<HashMap<String, String>> tipsForActivity = allTipData.get(activityName);
            if (tipsForActivity == null) {
                tipsForActivity = new ArrayList<>();
            }
            tipsForActivity.add(tip);
            allTipData.put(activityName, tipsForActivity);
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
            return new ToolTipComposer(allTipData, globalConfig);
        }
    }
}
