package com.ramaraj.tooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToolTipBuilder {
    private HashMap<String, List<StaticTip>> allTips;

    public ToolTipBuilder() {
        allTips = new HashMap<>();
    }

    public void addStaticTips(String activityName, int[] identifiers, String[] tipsText) {
        List<StaticTip> tipsForActivity = staticTipsForActivity(activityName);
        for (int i = 0; i < identifiers.length; i++) {
            StaticTip tip = new StaticTip(activityName, identifiers[i], tipsText[i]);
            tipsForActivity.add(tip);
        }
        allTips.put(activityName, tipsForActivity);
    }

    public List<StaticTip> staticTipsForActivity(String activityName) {
        if (allTips.get(activityName) == null) {
            allTips.put(activityName, new ArrayList<StaticTip>());
        }
        return allTips.get(activityName);
    }
}
