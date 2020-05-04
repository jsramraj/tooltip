package com.ramaraj.tooltip;

/**
 * Listen to the tip events. Use these methods in activity level.
 * Make sure the activities implements these methods
 */
public class ToolTipListener {
    public interface ToolTipOnDismissListener {
        /**
         * Gets called whenever a tip gets dismissed
         * @param tip The tip that was dismissed
         */
        void onTipDismissed(ToolTip tip);
    }

    public interface ToolTipOnShowListener {
        /**
         * Gets called whenever a tip is displayed to the user
         * @param tip The tip that was shown
         */
        void onTipShown(ToolTip tip);
    }

    public interface ToolTipConfigChange {
        /**
         * Override this method, if you want to customize the appearance of a specific tip.
         * @param tip The tip to which you want to customize
         * @return an instance of {@code ToolTipConfig} object
         */
        ToolTipConfig configForTip(ToolTip tip);
    }
}
