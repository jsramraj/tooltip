package com.navram.tooltip;

import com.navram.tooltip.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Construct the tooltips from string values.
 */
class ToolTipBuilder {

    private List<StaticTip> allTips;

    ToolTipBuilder() {
        allTips = new ArrayList<>();
    }

    /**
     * Add the tooltip data to construct the static tip.
     *
     * @param activityName Local class name of the activity
     * @param identifier   Array of the resource id of the view for which the user has acknowledged the tip
     * @param title        Array of the tip titles (Optional)
     * @param message      Array of tip message
     */
    void addStaticTip(String activityName, int identifier, String title, String message) {
        // Check if the tip is already displayed to the user
        if (!PersistentManager.getInstance().isAcknowledged(activityName, identifier)) {
            allTips.add(new StaticTip(activityName, identifier, title, message));
        }
    }

    /**
     * Construct static tooltip from tooltip
     *
     * @param activityName Local class name of the activity
     * @return Array fo Static tool tip objects for the given activity
     */
    List<StaticTip> staticTipsForActivity(String activityName) {
        return ResourceUtils.findStaticToolTipItems(allTips, activityName);
    }
}
