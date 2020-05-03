package com.ramaraj.tooltip;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ramaraj.tooltip.utils.StatusBarUtils;
import com.ramaraj.tooltip.utils.StringUtils;

public class StaticTip extends ToolTip {

    private PopupWindow tipPopupWindow;

    public StaticTip(String activityName, int resourceId, String tipTitle, String tipText) {
        super(activityName, resourceId, tipTitle, tipText);
    }

    @Override
    public void displayTip(Activity activity) {

        View targetView = activity.findViewById(resourceId);
        if (targetView == null) {
            //No view with this id is found in the activity
            return;
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        View tipView = LayoutInflater.from(activity).inflate(R.layout.tooltip_content, null);
        TextView tipTextView = tipView.findViewById(R.id.hint_text);
        TextView tipTitleTextView = tipView.findViewById(R.id.tip_title);
        SeeThroughViewGroup holeView = tipView.findViewById(R.id.see_through_view);
        Button nextButton = tipView.findViewById(R.id.nextButton);

        tipTextView.setText(tipText);
        if (ToolTipConfig.getInstance().getTipTextStyleResId() > 0) {
            tipTextView.setTextAppearance(ToolTipConfig.getInstance().getTipTextStyleResId());
        }
        if (activity instanceof ToolTipListener.ToolTipConfigChange) {
            ToolTipConfig config = ((ToolTipListener.ToolTipConfigChange) activity).configForTip(this);
            if (config != null && config.getTipTextStyleResId() > 0)
                tipTextView.setTextAppearance(config.getTipTextStyleResId());
        }

        Rect targetViewFrame = new Rect();
        targetView.getGlobalVisibleRect(targetViewFrame);

        int pos[] = new int[2];
        targetView.getLocationOnScreen(pos);

        int paddingForRect = 20;
        FrameLayout.LayoutParams holeViewLayoutParams = (FrameLayout.LayoutParams) holeView.getLayoutParams();
        holeViewLayoutParams.leftMargin = targetViewFrame.left - paddingForRect;
        holeViewLayoutParams.topMargin = targetViewFrame.top - StatusBarUtils.getStatusBarOffset(activity) - paddingForRect;
        holeViewLayoutParams.width = targetViewFrame.width() + 2 * paddingForRect;
        holeViewLayoutParams.height = targetViewFrame.height() + 2 * paddingForRect;
        holeView.setCornerRadius(50);
        holeView.setLayoutParams(holeViewLayoutParams);

        tipTextView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = tipTextView.getMeasuredHeight();
        int bottomSpace = 50;
        FrameLayout.LayoutParams tipTextViewParams = (FrameLayout.LayoutParams) tipTextView.getLayoutParams();
        tipTextViewParams.leftMargin = 0;
        tipTextViewParams.topMargin = holeViewLayoutParams.topMargin - height - bottomSpace;
        tipTextView.setLayoutParams(tipTextViewParams);

        tipTitleTextView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int titleHeight = tipTextView.getMeasuredHeight();
        FrameLayout.LayoutParams titleTitleParams = (FrameLayout.LayoutParams) tipTitleTextView.getLayoutParams();
        titleTitleParams.leftMargin = 0;
        titleTitleParams.topMargin = tipTextViewParams.topMargin - titleHeight;
        tipTitleTextView.setLayoutParams(titleTitleParams);

        tipTitleTextView.setText(tipTitle);
        tipTitleTextView.setVisibility(StringUtils.isNullOrEmpty(tipTitle) ? View.INVISIBLE : View.VISIBLE);
        if (!StringUtils.isNullOrEmpty(tipTitle)) {
            if (ToolTipConfig.getInstance().getTipTitleTextStyleResId() > 0) {
                tipTitleTextView.setTextAppearance(ToolTipConfig.getInstance().getTipTitleTextStyleResId());
            }
            if (activity instanceof ToolTipListener.ToolTipConfigChange) {
                ToolTipConfig config = ((ToolTipListener.ToolTipConfigChange) activity).configForTip(this);
                if (config != null && config.getTipTitleTextStyleResId() > 0)
                    tipTitleTextView.setTextAppearance(config.getTipTitleTextStyleResId());
            }
        }


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissTip();
            }
        });

        tipPopupWindow = new PopupWindow(tipView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        tipPopupWindow.showAtLocation(targetView, Gravity.TOP | Gravity.RIGHT, 0, 0);

        super.displayTip(activity);
    }

    @Override
    public void dismissTip() {
        if (tipPopupWindow != null)
            tipPopupWindow.dismiss();
        super.dismissTip();
    }
}
