package com.ramaraj.tooltip;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
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

import com.ramaraj.tooltip.utils.StatusBarUtils;

public class StaticTip extends ToolTip implements IToolTip {

    private ToolTipListener.ToolTipOnDismissListener listener;
    private PopupWindow tipPopupWindow;

    public StaticTip(String activityName, int resourceId, String tipText) {
        super(activityName, resourceId, tipText);
    }

    @Override
    public void displayTip(Context context) {
        final Activity activity = (Activity) context;
        View targetView = activity.findViewById(resourceId);
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
        if (ToolTipConfig.getInstance().getStyleResId() > 0) {
            tipTextView.setTextAppearance(ToolTipConfig.getInstance().getStyleResId());
        }
        if (activity instanceof ToolTipListener.ToolTipConfigChange) {
            ToolTipConfig config = ((ToolTipListener.ToolTipConfigChange) activity).configForTip(this);
            if (config != null && config.getStyleResId() > 0)
                tipTextView.setTextAppearance(config.getStyleResId());
        }

        Rect myViewRect2 = new Rect();
        hintView.getGlobalVisibleRect(myViewRect2);
        Log.d("TTA", String.valueOf(myViewRect2));
        Rect targetViewFrame = new Rect();
        targetView.getGlobalVisibleRect(targetViewFrame);

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
        int pos[] = new int[2];
        targetView.getLocationOnScreen(pos);

        int paddingForRect = 20;
        FrameLayout.LayoutParams holeViewLayoutParams = (FrameLayout.LayoutParams) holeView.getLayoutParams();
        holeViewLayoutParams.leftMargin = targetViewFrame.left - paddingForRect;
        holeViewLayoutParams.topMargin = targetViewFrame.top - StatusBarUtils.getStatusBarOffset(context) - paddingForRect;
        holeViewLayoutParams.width = targetViewFrame.width() + 2 * paddingForRect;
        holeViewLayoutParams.height = targetViewFrame.height() +2 * paddingForRect;
        holeView.setCornerRadius(50);
        holeView.setLayoutParams(holeViewLayoutParams);

        tipTextView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = tipTextView.getMeasuredHeight();
        int bottomSpace = 50;

        FrameLayout.LayoutParams tipTextViewParams = (FrameLayout.LayoutParams) tipTextView.getLayoutParams();
        tipTextViewParams.leftMargin = 0;
        tipTextViewParams.topMargin = holeViewLayoutParams.topMargin-height-bottomSpace;
        tipTextView.setLayoutParams(tipTextViewParams);

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
