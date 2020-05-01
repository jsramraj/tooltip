package com.ramaraj.tooltip;

import java.util.HashMap;

public class ToolTipComposer {

    private HashMap<String, String[]> tipIdentifiers;
    private HashMap<String, String[]> tipsText;

    public ToolTipComposer() {
        this.tipIdentifiers = new HashMap<>();
        this.tipsText = new HashMap<>();
    }

    public void addStaticTips(String activityName, String[] identifiers, String[] tips) {
        tipIdentifiers.put(activityName, identifiers);
        tipsText.put(activityName, tips);
    }

    public String[] tipIdentifiers(String activityName) {
        return tipIdentifiers.get(activityName);
    }

    public String[] tipTexts(String activityName) {
        return tipsText.get(activityName);
    }
}
