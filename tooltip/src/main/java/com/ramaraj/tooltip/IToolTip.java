package com.ramaraj.tooltip;

import android.app.Activity;

public interface IToolTip {

    /**
     * @param activity to display the tooltip view
     */
    void displayTip(Activity activity);

    /**
     * dismissing the tooltip view
     */
    void dismissTip();
}
