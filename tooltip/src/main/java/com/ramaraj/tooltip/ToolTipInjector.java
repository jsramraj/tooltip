package com.ramaraj.tooltip;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ramaraj.tooltip.utils.ResourceUtils;

public class ToolTipInjector {

    public static void init(Application application) {

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(@NonNull final Activity activity) {
                View activityView = activity.findViewById(android.R.id.content);
                activityView.post(new Runnable() {
                    @Override
                    public void run() {
                        IToolTip toolTip = new StaticTip(activity, activity.getResources().getIdentifier("helloWorldLabel", "id", activity.getPackageName()), "Tip for Hello World text field");
                        toolTip.displayTip();
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
