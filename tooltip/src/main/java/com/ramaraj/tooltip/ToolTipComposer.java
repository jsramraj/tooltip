package com.ramaraj.tooltip;

import java.util.HashMap;

public class ToolTipComposer {

    private HashMap<String, String[]> tipIdentifiers;
    private HashMap<String, String[]> tipTitles;
    private HashMap<String, String[]> tipsText;

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

    public static class Builder {
        private HashMap<String, String[]> tipIdentifiers;
        private HashMap<String, String[]> tipTitles;
        private HashMap<String, String[]> tipsText;
        private ToolTipConfig globalConfig;

        public Builder() {
            this.tipIdentifiers = new HashMap<>();
            this.tipTitles = new HashMap<>();
            this.tipsText = new HashMap<>();
        }

        public Builder addStaticTips(String activityName, String[] identifiers, String[] titles, String[] tips) {
            tipIdentifiers.put(activityName, identifiers);
            tipTitles.put(activityName, titles);
            tipsText.put(activityName, tips);
            return this;
        }

        public Builder setGlobalConfig(ToolTipConfig globalConfig) {
            this.globalConfig = globalConfig;
            return this;
        }

        public ToolTipComposer build() {
            return new ToolTipComposer(tipIdentifiers, tipTitles, tipsText, globalConfig);
        }
    }
}
