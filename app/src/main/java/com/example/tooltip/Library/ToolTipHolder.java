package com.example.tooltip.Library;

import android.app.Activity;
import android.content.Context;
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

import com.example.tooltip.R;

public class ToolTipHolder {

    public static void showTip(Activity activity, int resourceId, String tipText) {
        View activityView = activity.findViewById(android.R.id.content);
        View view = activity.findViewById(resourceId);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / 2.325);
        int height = (int) (displayMetrics.heightPixels / 2.325);

        View tipView = LayoutInflater.from(activity).inflate(R.layout.tooltip_content, null);
        TextView tipTextView = tipView.findViewById(R.id.hint_text);
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
//        param.width = view.getMeasuredWidth();
        tipTextView.setLayoutParams(param);


        PopupWindow tipWindow = new PopupWindow(tipView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        tipWindow.showAtLocation(view, Gravity.TOP|Gravity.RIGHT, 0, 0);
    }
}
