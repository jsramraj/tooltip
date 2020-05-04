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

/**
 * Tips for the static controls
 * If multiple static tips are added, they will be shown one by one
 */
public class StaticTip extends ToolTip {

    private PopupWindow tipPopupWindow;

    public StaticTip(String activityName, int resourceId, String tipTitle, String tipText) {
        super(activityName, resourceId, tipTitle, tipText);
    }

    @Override
    public boolean displayTip(Activity activity) {

        View targetView = activity.findViewById(resourceId);
        if (targetView == null) {
            //No view with this id is found in the activity
            return false;
        }

        Rect targetViewFrame = new Rect();
        targetView.getGlobalVisibleRect(targetViewFrame);
        if (targetViewFrame.isEmpty()) {
            //the view frame is not valid, or the view's visiblity status is .GONE
            return false;
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // Inflate all the controls from the tip layout
        View tipView = LayoutInflater.from(activity).inflate(R.layout.tooltip_content, null);
        TextView tipMessageTextView = tipView.findViewById(R.id.hint_text);
        TextView tipTitleTextView = tipView.findViewById(R.id.tip_title);
        SeeThroughViewGroup holeView = tipView.findViewById(R.id.see_through_view);
        Button nextButton = tipView.findViewById(R.id.nextButton);

        tipMessageTextView.setText(tipText);
        // Check if custom style is found in activity level
        if (ToolTipConfig.getInstance().getTipMessageStyleResId() > 0) {
            tipMessageTextView.setTextAppearance(ToolTipConfig.getInstance().getTipMessageStyleResId());
        }
        // Check if custom style is found in global level
        if (activity instanceof ToolTipListener.ToolTipConfigChange) {
            ToolTipConfig config = ((ToolTipListener.ToolTipConfigChange) activity).configForTip(this);
            if (config != null && config.getTipMessageStyleResId() > 0)
                tipMessageTextView.setTextAppearance(config.getTipMessageStyleResId());
        }

        int pos[] = new int[2];// location of the target view
        targetView.getLocationOnScreen(pos);

        // set the frame for the hole view
        int paddingForRect = 20;
        FrameLayout.LayoutParams holeViewLayoutParams = (FrameLayout.LayoutParams) holeView.getLayoutParams();
        holeViewLayoutParams.leftMargin = targetViewFrame.left - paddingForRect;
        holeViewLayoutParams.topMargin = targetViewFrame.top - StatusBarUtils.getStatusBarOffset(activity) - paddingForRect;
        holeViewLayoutParams.width = targetViewFrame.width() + 2 * paddingForRect;
        holeViewLayoutParams.height = targetViewFrame.height() + 2 * paddingForRect;
        holeView.setCornerRadius(50);
        holeView.setLayoutParams(holeViewLayoutParams);

        // set the frame for the tip description
        tipMessageTextView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = tipMessageTextView.getMeasuredHeight();
        int bottomSpace = 25;
        FrameLayout.LayoutParams tipTextViewParams = (FrameLayout.LayoutParams) tipMessageTextView.getLayoutParams();
        tipTextViewParams.leftMargin = 0;
        tipTextViewParams.topMargin = holeViewLayoutParams.topMargin - height - bottomSpace;
        tipMessageTextView.setLayoutParams(tipTextViewParams);

        // set the frame for the tip title
        tipTitleTextView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int titleHeight = tipMessageTextView.getMeasuredHeight();
        FrameLayout.LayoutParams titleTitleParams = (FrameLayout.LayoutParams) tipTitleTextView.getLayoutParams();
        titleTitleParams.leftMargin = 0;
        titleTitleParams.topMargin = tipTextViewParams.topMargin - titleHeight - bottomSpace;
        tipTitleTextView.setLayoutParams(titleTitleParams);

        tipTitleTextView.setText(tipTitle);
        // if the title for the tip is not registered, let's hide the tip title textview
        tipTitleTextView.setVisibility(StringUtils.isNullOrEmpty(tipTitle) ? View.INVISIBLE : View.VISIBLE);
        if (!StringUtils.isNullOrEmpty(tipTitle)) {
            // Check if custom style is found in activity level
            if (ToolTipConfig.getInstance().getTipTitleStyleResId() > 0) {
                tipTitleTextView.setTextAppearance(ToolTipConfig.getInstance().getTipTitleStyleResId());
            }
            // Check if custom style is found in global level
            if (activity instanceof ToolTipListener.ToolTipConfigChange) {
                ToolTipConfig config = ((ToolTipListener.ToolTipConfigChange) activity).configForTip(this);
                if (config != null && config.getTipTitleStyleResId() > 0)
                    tipTitleTextView.setTextAppearance(config.getTipTitleStyleResId());
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

        return super.displayTip(activity);
    }

    @Override
    public void dismissTip() {
        if (tipPopupWindow != null)
            tipPopupWindow.dismiss();
        super.dismissTip();
    }
}
