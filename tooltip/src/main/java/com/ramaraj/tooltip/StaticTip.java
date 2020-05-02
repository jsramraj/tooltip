package com.ramaraj.tooltip;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * To show tooltip view in the activity page specified by the user.
 */
public class StaticTip implements IToolTip {

    private final int resourceId;
    private final String tipText;
    private final String activityName;
    private ToolTipListener listener;
    private PopupWindow tipPopupWindow;

    public StaticTip(String activityName, int resourceId, String tipText) {
        this.activityName = activityName;
        this.resourceId = resourceId;
        this.tipText = tipText;
    }

    @Override
    public void displayTip(Activity activity) {

        View hintView = activity.findViewById(resourceId);

        int statusBarSize = (int) activity.getResources().getDimension(R.dimen.status_bar_size);
        int defaultMargin = (int) activity.getResources().getDimension(R.dimen.margin_16dp);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        View tipView = LayoutInflater.from(activity).inflate(R.layout.tooltip_content, null);
        TextView tipTextView = tipView.findViewById(R.id.hint_text);
        SeeThroughViewGroup holeView = tipView.findViewById(R.id.see_through_view);
        Button nextButton = tipView.findViewById(R.id.nextButton);

        tipTextView.setText(tipText);

        Rect myViewRect2 = new Rect();
        hintView.getGlobalVisibleRect(myViewRect2);
        Log.d("TTA", String.valueOf(myViewRect2));

        int[] pos = new int[2];
        hintView.getLocationOnScreen(pos);
        Log.d("TTA", String.format("%d, %d, %d, %d", pos[0], pos[1], hintView.getMeasuredWidth(), hintView.getMeasuredHeight()));

        int width = hintView.getMeasuredWidth();
        int height = hintView.getMeasuredHeight();

        if (width > height) {
            height = width;
        } else {
            width = height;
        }

        height += defaultMargin;
        width += defaultMargin;

        FrameLayout.LayoutParams labelParams = (FrameLayout.LayoutParams) tipTextView.getLayoutParams();
        labelParams.leftMargin = pos[0];
        labelParams.topMargin = pos[1] - hintView.getMeasuredHeight() * 3;

        labelParams.height = hintView.getMeasuredHeight();
        tipTextView.setLayoutParams(labelParams);

        FrameLayout.LayoutParams holeViewParams = (FrameLayout.LayoutParams) holeView.getLayoutParams();

        holeViewParams.height = height;
        holeViewParams.width = width;

        holeViewParams.leftMargin = (int) hintView.getX() - (defaultMargin / 2);
        holeViewParams.topMargin = (int) hintView.getY() + statusBarSize  - (defaultMargin / 2);

        holeView.setLayoutParams(holeViewParams);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissTip();
            }
        });

        tipPopupWindow = new PopupWindow(tipView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        tipPopupWindow.showAtLocation(hintView, Gravity.TOP | Gravity.RIGHT, 0, 0);
    }

    @Override
    public void dismissTip() {
        if (tipPopupWindow != null)
            tipPopupWindow.dismiss();

        if (listener != null) {
            listener.onTipDismissed(StaticTip.this);
        }
    }

    public void setListener(ToolTipListener listener) {
        this.listener = listener;
    }
}
