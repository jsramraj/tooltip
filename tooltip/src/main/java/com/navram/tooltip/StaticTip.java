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
import android.widget.LinearLayout;
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
        LinearLayout textLayout = tipView.findViewById(R.id.text_layout);
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

        // set the frame for the title and description textviews
        int availableWidth = displayMetrics.widthPixels;

        int widthSpec = View.MeasureSpec.makeMeasureSpec(availableWidth, View.MeasureSpec.AT_MOST);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tipMessageTextView.measure(widthSpec, heightSpec);
        tipTitleTextView.measure(widthSpec, heightSpec);

        int paddingOverTargetView = 50;
        FrameLayout.LayoutParams textViewLayoutParams = (FrameLayout.LayoutParams) textLayout.getLayoutParams();
        textViewLayoutParams.leftMargin = 0;
        textViewLayoutParams.topMargin = holeViewLayoutParams.topMargin
                - tipMessageTextView.getMeasuredHeight()
                - tipTitleTextView.getMeasuredHeight()
                - paddingOverTargetView;

        textLayout.setLayoutParams(normalizeLayoutParams(aActivity, textViewLayoutParams));


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

    /**
     * If the layout params goes outside the visible area of the activity's frame, this method will correct it.
     * @param aActivity Target view's parent activity
     * @param layoutParams Layout params of the title layout to normalize
     * @return Normalized frame
     */
    private FrameLayout.LayoutParams normalizeLayoutParams(Activity aActivity, FrameLayout.LayoutParams layoutParams) {
        View content = aActivity.findViewById(android.R.id.content);
        ViewGroup.LayoutParams contentLayoutParams = content.getLayoutParams();
        layoutParams.leftMargin = Math.max(layoutParams.leftMargin, 0);
        layoutParams.topMargin = Math.max(layoutParams.topMargin, 0);
        if (layoutParams.leftMargin + layoutParams.width > contentLayoutParams.width) {
            layoutParams.leftMargin -= layoutParams.width;
        }
        if (layoutParams.topMargin + layoutParams.height > contentLayoutParams.height) {
            layoutParams.topMargin -= layoutParams.height;
        }
        return layoutParams;
    }

    @Override
    public void dismissTip() {
        if (tipPopupWindow != null)
            tipPopupWindow.dismiss();
        super.dismissTip();
    }
}
