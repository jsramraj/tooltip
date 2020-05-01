package com.ramaraj.tooltip;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ramaraj.tooltip.utils.ResourceUtils;

public class ToolTipInjector {

    private static ToolTipBuilder tipBuilder;

    public static void init(final Application application, final ToolTipComposer tipComposer) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                int[] identifiers = ResourceUtils.getResourceIdentifiers(activity, tipComposer.tipIdentifiers(activity.getLocalClassName()));
                tipBuilder = new ToolTipBuilder();
                tipBuilder.addStaticTips(activity.getLocalClassName(), identifiers, tipComposer.tipTexts(activity.getLocalClassName()));
            }

            @Override
            public void onActivityStarted(@NonNull final Activity activity) {
                View activityView = activity.findViewById(android.R.id.content);
                activityView.post(new Runnable() {
                    @Override
                    public void run() {
                        ToolTipPresenter.displayStaticTipsForActivity(tipBuilder, activity);
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
