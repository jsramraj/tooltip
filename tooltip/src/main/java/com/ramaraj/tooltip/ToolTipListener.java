package com.ramaraj.tooltip;

public class ToolTipListener {
    public interface ToolTipOnDismissListener {
        void onTipDismissed(ToolTip tip);
    }

    public interface ToolTipOnShowListener {
        void onTipShown(ToolTip tip);
    }

    public interface ToolTipConfigChange {
        ToolTipConfig configForTip(ToolTip tip);
    }
}
