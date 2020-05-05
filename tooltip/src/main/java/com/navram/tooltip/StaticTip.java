package com.navram.tooltip;

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

import com.navram.tooltip.utils.StatusBarUtils;
import com.navram.tooltip.utils.StringUtils;

/**
 * Tips for the static controls
 * If multiple static tips are added, they will be shown one by one
 */
public class StaticTip extends ToolTip {

    private PopupWindow tipPopupWindow;

    StaticTip(String activityName, int resourceId, String tipTitle, String tipText) {
        super(activityName, resourceId, tipTitle, tipText);
    }

    @Override
    public boolean displayTip(Activity activity) {

        if (PersistentManager.getInstance().isAcknowledged(getActivityName(), getResourceId())) {
            return false;
        }

        View targetView = activity.findViewById(getResourceId());

        if (targetView == null) {
            //No view with this id is found in the activity
            return false;
        }

        Rect targetViewFrame = new Rect();
        targetView.getGlobalVisibleRect(targetViewFrame);

        if (targetViewFrame.isEmpty()) {
            //the view frame is not valid, or the view's visibility status is View.GONE
            return false;
        }

        createToolTipWindow(activity, targetView, targetViewFrame);

        return super.displayTip(activity);
    }

    private void createToolTipWindow(Activity aActivity, View aTargetView, Rect targetViewFrame) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        aActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // Inflate all the controls from the tip layout
        View tipView = LayoutInflater.from(aActivity).inflate(R.layout.tooltip_content, null);
        TextView tipMessageTextView = tipView.findViewById(R.id.hint_text);
        TextView tipTitleTextView = tipView.findViewById(R.id.tip_title);
        SeeThroughViewGroup holeView = tipView.findViewById(R.id.see_through_view);
        Button nextButton = tipView.findViewById(R.id.nextButton);

        tipMessageTextView.setText(getTipText());
        // Check if custom style is found in aActivity level
        if (ToolTipConfig.getInstance().getTipMessageStyleResId() > 0) {
            tipMessageTextView.setTextAppearance(ToolTipConfig.getInstance().getTipMessageStyleResId());
        }
        // Check if custom style is found in global level
        if (aActivity instanceof ToolTipListener.ToolTipConfigChange) {
            ToolTipConfig config = ((ToolTipListener.ToolTipConfigChange) aActivity).configForTip(this);
            if (config != null && config.getTipMessageStyleResId() > 0)
                tipMessageTextView.setTextAppearance(config.getTipMessageStyleResId());
        }

        int[] pos = new int[2];// location of the target view
        aTargetView.getLocationOnScreen(pos);

        // set the frame for the hole view
        int paddingForRect = 20;
        FrameLayout.LayoutParams holeViewLayoutParams = (FrameLayout.LayoutParams) holeView.getLayoutParams();
        holeViewLayoutParams.leftMargin = targetViewFrame.left - paddingForRect;
        holeViewLayoutParams.topMargin = targetViewFrame.top - StatusBarUtils.getStatusBarOffset(aActivity) - paddingForRect;
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

        tipTitleTextView.setText(getTipTitle());
        // if the title for the tip is not registered, let's hide the tip title textview
        tipTitleTextView.setVisibility(StringUtils.isNullOrEmpty(getTipTitle()) ? View.INVISIBLE : View.VISIBLE);
        if (!StringUtils.isNullOrEmpty(getTipTitle())) {
            // Check if custom style is found in aActivity level
            if (ToolTipConfig.getInstance().getTipTitleStyleResId() > 0) {
                tipTitleTextView.setTextAppearance(ToolTipConfig.getInstance().getTipTitleStyleResId());
            }
            // Check if custom style is found in global level
            if (aActivity instanceof ToolTipListener.ToolTipConfigChange) {
                ToolTipConfig config = ((ToolTipListener.ToolTipConfigChange) aActivity).configForTip(this);
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
        tipPopupWindow.showAtLocation(aTargetView, Gravity.TOP | Gravity.END, 0, 0);
    }

    @Override
    public void dismissTip() {
        if (tipPopupWindow != null)
            tipPopupWindow.dismiss();
        super.dismissTip();
    }
}
