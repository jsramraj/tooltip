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
     * All the tips for all the activities
     * It is required that all three arrays here are not null and have same length
     *
     * @param activityName Local class name of the activity
     * @param identifiers Array of the resource id of the view for which the user has acknowledged the tip
     * @param tipsTitles Array of the tip titles (Optional)
     * @param tipsText Array of tip message
     */
    public void addStaticTips(String activityName, int[] identifiers, String[] tipsTitles, String[] tipsText) {
        List<StaticTip> tipsForActivity = staticTipsForActivity(activityName);
        for (int index = 0; index < identifiers.length; index++) {
            if (PersistentManager.getInstance().isAcknowledged(activityName, identifiers[index])) {
                //we have already shown the tip for this resource
                //let's skip creating this tip
                continue;
            }
            StaticTip tip = new StaticTip(activityName, identifiers[index], tipsTitles[index], tipsText[index]);
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
            allTips.put(activityName, new ArrayList<StaticTip>());
        }
        return allTips.get(activityName);
    }
}
