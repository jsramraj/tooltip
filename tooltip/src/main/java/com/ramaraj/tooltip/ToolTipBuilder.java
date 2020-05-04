package com.ramaraj.tooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Construct the tooltips from string values.
 */
public class ToolTipBuilder {

    private HashMap<String, List<StaticTip>> allTips;

    public ToolTipBuilder() {
        allTips = new HashMap<>();
    }

    /**
     * Add the tooltip data to construct the static tip.
     *
     * @param activityName Local class name of the activity
     * @param identifier  Array of the resource id of the view for which the user has acknowledged the tip
     * @param title       Array of the tip titles (Optional)
     * @param message     Array of tip message
     */
    public void addStaticTip(String activityName, int identifier, String title, String message) {
        List<StaticTip> tipsForActivity = staticTipsForActivity(activityName);
        if (!PersistentManager.getInstance().isAcknowledged(activityName, identifier)) {
            StaticTip tip = new StaticTip(activityName, identifier, title, message);
            tipsForActivity.add(tip);
        }
        allTips.put(activityName, tipsForActivity);
    }

    /**
     * Construct static tooltip from tooltip
     * @param activityName Local class name of the activity
     * @return Array fo Static tool tip objects for the given activity
     */
    public List<StaticTip> staticTipsForActivity(String activityName) {
        if (allTips.get(activityName) == null) {
            allTips.put(activityName, new ArrayList<>());
        }
        return allTips.get(activityName);
    }
}
