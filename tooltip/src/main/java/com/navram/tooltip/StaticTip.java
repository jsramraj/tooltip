package com.navram.tooltip;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.navram.tooltip.utils.GeometryUtils;
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
        TextView tipMessageTextView = tipView.findViewById(R.id.tip_message);
        TextView tipTitleTextView = tipView.findViewById(R.id.tip_title);
        SeeThroughViewGroup holeView = tipView.findViewById(R.id.see_through_view);
        Button nextButton = tipView.findViewById(R.id.nextButton);

        tipMessageTextView.setText(getTipText());
        tipTitleTextView.setText(getTipTitle());
        // if the title for the tip is not registered, let's hide the tip title textview
        tipTitleTextView.setVisibility(StringUtils.isNullOrEmpty(getTipTitle()) ? View.INVISIBLE : View.VISIBLE);

        int[] pos = new int[2];// location of the target view
        aTargetView.getLocationOnScreen(pos);

        // set the frame for the hole view
        int paddingForRect = 20;
        int cornerRadius = 50;

        float deviation = 0.1f;//10 % deviation
        int holeWidth = targetViewFrame.width() + 2 * paddingForRect;
        int holeHeight = targetViewFrame.height() + 2 * paddingForRect;

        // check if the frame of the target view is a square (with given deviation)
        if (GeometryUtils.isAlmostSquare(holeWidth, holeHeight, deviation)) {
            // let's draw a circle instead of rounded rectangle
            cornerRadius = Math.max(holeWidth, holeHeight) / 2;
            holeWidth = holeHeight = Math.max(holeWidth, holeHeight);
        }

        int topMargin = StatusBarUtils.getStatusBarOffset(aActivity) + paddingForRect;

        ConstraintLayout.LayoutParams holeViewLayoutParams = (ConstraintLayout.LayoutParams) holeView.getLayoutParams();
        holeViewLayoutParams.leftMargin = targetViewFrame.left - paddingForRect;
        holeViewLayoutParams.topMargin = targetViewFrame.top - topMargin;
        holeViewLayoutParams.width = holeWidth;
        holeViewLayoutParams.height = holeHeight;

        holeView.setCornerRadius(cornerRadius);
        holeView.setLayoutParams(holeViewLayoutParams);

        boolean isTitleHasToShow = !StringUtils.isNullOrEmpty(getTipTitle());

        if (isTitleHasToShow &&
                holeViewLayoutParams.topMargin < (tipTitleTextView.getHeight() + tipMessageTextView.getHeight()) ||
                holeViewLayoutParams.topMargin < (tipMessageTextView.getHeight())) {


            //Changing the constraints for tip message and title view of the tooltip window

            ConstraintLayout.LayoutParams titleParams = (ConstraintLayout.LayoutParams) tipTitleTextView.getLayoutParams();
            titleParams.topToBottom = holeView.getId();
            titleParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            titleParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            titleParams.bottomToTop = ConstraintLayout.LayoutParams.UNSET;

            ConstraintLayout.LayoutParams messageParams = (ConstraintLayout.LayoutParams) tipMessageTextView.getLayoutParams();
            messageParams.topToBottom = tipTitleTextView.getId();
            messageParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            messageParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            messageParams.bottomToTop = ConstraintLayout.LayoutParams.UNSET;

            tipTitleTextView.requestLayout();
            tipMessageTextView.requestLayout();
        }

        // check if the user has override the config method to customize the styling
        tipMessageTextView.setTextAppearance(super.messageTextViewStyleId(aActivity));

        if (isTitleHasToShow) {
            tipTitleTextView.setTextAppearance(super.titleTextViewStyleId(aActivity));
        }
        nextButton.setTextAppearance(super.nextButtonStyleId(aActivity));
        tipView.setBackgroundColor(super.overlayBackgroundColor(aActivity));

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissTip();
            }
        });

        tipPopupWindow = new PopupWindow(tipView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        tipPopupWindow.showAtLocation(aTargetView, Gravity.TOP | Gravity.END, 0, 0);

        tipView.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (holeView.getBottom() >= nextButton.getTop()) {

                    ConstraintLayout.LayoutParams nextButtonParams = (ConstraintLayout.LayoutParams) nextButton.getLayoutParams();
                    nextButtonParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                    nextButtonParams.startToStart = ConstraintLayout.LayoutParams.UNSET;
                    nextButtonParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
                    nextButtonParams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET;

                    nextButton.requestLayout();
                }
            }
        }, 250);
    }

    @Override
    public void dismissTip() {
        if (tipPopupWindow != null)
            tipPopupWindow.dismiss();
        super.dismissTip();
    }
}
