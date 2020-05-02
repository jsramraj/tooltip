package com.ramaraj.tooltip;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
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

public class StaticTip extends ToolTip implements IToolTip {

    private ToolTipListener.ToolTipOnDismissListener listener;
    private PopupWindow tipPopupWindow;

    public StaticTip(String activityName, int resourceId, String tipText) {
        super(activityName, resourceId, tipText);
    }

    private int getStatusBarOffset(Context context) {
        int result = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }

        return result;
    }

    @Override
    public void displayTip(Context context) {
        final Activity activity = (Activity) context;
        View targetView = activity.findViewById(resourceId);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        View tipView = LayoutInflater.from(activity).inflate(R.layout.tooltip_content, null);
        TextView tipTextView = tipView.findViewById(R.id.hint_text);
        SeeThroughViewGroup holeView = tipView.findViewById(R.id.see_through_view);
        Button nextButton = tipView.findViewById(R.id.nextButton);

        tipTextView.setText(tipText);

        Rect targetViewFrame = new Rect();
        targetView.getGlobalVisibleRect(targetViewFrame);
        Log.d("TTA", String.valueOf(targetViewFrame));

        int pos[] = new int[2];
        targetView.getLocationOnScreen(pos);
        Log.d("TTA", String.format("%d, %d, %d, %d", pos[0], pos[1], targetView.getMeasuredWidth(), targetView.getMeasuredHeight()));

        int padding = 20;
        FrameLayout.LayoutParams param2 = (FrameLayout.LayoutParams) holeView.getLayoutParams();
        param2.leftMargin = targetViewFrame.left - padding;
        param2.topMargin = targetViewFrame.top - getStatusBarOffset(context) - padding;
        param2.width = targetViewFrame.width() + 2 * padding;
        param2.height = targetViewFrame.height() +2 * padding;
        holeView.setCornerRadius(50);
        holeView.setLayoutParams(param2);

        FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) tipTextView.getLayoutParams();
        param.leftMargin = 0;
        param.topMargin = param2.topMargin - param2.height;
        param.height = targetView.getMeasuredHeight();
        tipTextView.setLayoutParams(param);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissTip();
            }
        });

        tipPopupWindow = new PopupWindow(tipView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        tipPopupWindow.showAtLocation(targetView, Gravity.TOP | Gravity.RIGHT, 0, 0);

        if (activity instanceof ToolTipListener.ToolTipOnShowListener) {
            ((ToolTipListener.ToolTipOnShowListener) activity).onTipShown(StaticTip.this);
        }
    }

    @Override
    public void dismissTip() {
        if (tipPopupWindow != null)
            tipPopupWindow.dismiss();

        if (listener != null) {
            listener.onTipDismissed(StaticTip.this);
        }
    }

    public void setListener(ToolTipListener.ToolTipOnDismissListener listener) {
        this.listener = listener;
    }
}
