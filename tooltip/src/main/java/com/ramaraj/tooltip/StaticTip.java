package com.ramaraj.tooltip;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class StaticTip implements IToolTip {

    private final Activity activity;
    private final int resourceId;
    private final String tipText;

    public StaticTip(Activity activity, int resourceId, String tipText) {
        this.activity = activity;
        this.resourceId = resourceId;
        this.tipText = tipText;
    }

    @Override
    public void displayTip() {
        View view = activity.findViewById(resourceId);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        View tipView = LayoutInflater.from(activity).inflate(R.layout.tooltip_content, null);
        TextView tipTextView = tipView.findViewById(R.id.hint_text);
        SeeThroughViewGroup holeView = tipView.findViewById(R.id.see_through_view);
        tipTextView.setText(tipText);

        Rect myViewRect2 = new Rect();
        view.getGlobalVisibleRect(myViewRect2);
        Log.d("TTA", String.valueOf(myViewRect2));

        int pos[] = new int[2];
        view.getLocationOnScreen(pos);
        Log.d("TTA", String.format("%d, %d, %d, %d", pos[0], pos[1], view.getMeasuredWidth(), view.getMeasuredHeight()));

        FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) tipTextView.getLayoutParams();
        param.leftMargin = pos[0];
        param.topMargin = pos[1] - view.getMeasuredHeight() * 3;
        param.height = view.getMeasuredHeight();
        tipTextView.setLayoutParams(param);

        FrameLayout.LayoutParams param2 = (FrameLayout.LayoutParams) holeView.getLayoutParams();
        param2.leftMargin = pos[0] + view.getMeasuredWidth() / 2 - param2.width/2;
        param2.topMargin = pos[1] - view.getMeasuredHeight() * 2;
        holeView.setLayoutParams(param2);

        PopupWindow tipWindow = new PopupWindow(tipView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        tipWindow.showAtLocation(view, Gravity.TOP|Gravity.RIGHT, 0, 0);
    }

    @Override
    public void dismissTip() {

    }
}
