package com.ramaraj.tooltip;

import java.util.HashMap;

public class ToolTipComposer {

    private HashMap<String, String[]> tipIdentifiers;
    private HashMap<String, String[]> tipsText;

    private ToolTipComposer(HashMap<String, String[]> tipIdentifiers, HashMap<String, String[]> tipsText, ToolTipConfig globalConfig) {
        this.tipIdentifiers = tipIdentifiers;
        this.tipsText = tipsText;
        ToolTipConfig.setInstance(globalConfig);
    }

    public String[] tipIdentifiers(String activityName) {
        return tipIdentifiers.get(activityName);
    }

    public String[] tipTexts(String activityName) {
        return tipsText.get(activityName);
    }

    public static class Builder {
        private HashMap<String, String[]> tipIdentifiers;
        private HashMap<String, String[]> tipsText;
        private ToolTipConfig globalConfig;

        public Builder() {
            this.tipIdentifiers = new HashMap<>();
            this.tipsText = new HashMap<>();
        }

        public Builder addStaticTips(String activityName, String[] identifiers, String[] tips) {
            tipIdentifiers.put(activityName, identifiers);
            tipsText.put(activityName, tips);
            return this;
        }

        public Builder setGlobalConfig(ToolTipConfig globalConfig) {
            this.globalConfig = globalConfig;
            return this;
        }

        public ToolTipComposer build() {
            return new ToolTipComposer(tipIdentifiers, tipsText, globalConfig);
        }
    }
}
