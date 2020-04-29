package com.example.tooltip;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tooltip.Library.ToolTipHolder;
import com.example.tooltip.Utils.ResourceUtils;

public class ToolTipInjector {

    public static void init(ToolTipApp toolTipApp) {

        toolTipApp.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(@NonNull final Activity activity) {
                Log.d("TTA", activity.getLocalClassName());
                Log.d("TTA", String.valueOf(ResourceUtils.getResId("helloWorldLabel", R.id.class)));

                View activityView = activity.findViewById(android.R.id.content);
                activityView.post(new Runnable() {
                    @Override
                    public void run() {
                        ToolTipHolder.showTip(activity, ResourceUtils.getResId("helloWorldLabel", R.id.class), "Tip for Hello World text field");
                    }
                });
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }
}
