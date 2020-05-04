package com.ramaraj.tooltip;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ramaraj.tooltip.utils.ResourceUtils;

public class ToolTipManager {

    private static ToolTipBuilder tipBuilder;

    public static void init(final Application application, final ToolTipComposer tipComposer) {
        PersistentManager.init(application);
        ActivityWrapper.init(tipComposer);

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                ActivityWrapper.buildTipsForActivity(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
            }

            @Override
            public void onActivityResumed(@NonNull final Activity activity) {
                View activityView = activity.findViewById(android.R.id.content);
                activityView.post(new Runnable() {
                    @Override
                    public void run() {
                        ActivityWrapper.showTipsForActivity(activity);
                    }
                });
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                ActivityWrapper.cleanUp();
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

    public static class ActivityWrapper {

        static ToolTipComposer tipComposer;
        static ToolTipPresenter toolTipPresenter;

        protected static void init(ToolTipComposer _tipComposer) {
            tipComposer = _tipComposer;
        }

        protected static void showTipsForActivity(Activity activity) {
            if (tipBuilder != null && tipBuilder.staticTipsForActivity(activity.getLocalClassName()).size() > 0) {
                toolTipPresenter = new ToolTipPresenter(tipBuilder, activity);
                toolTipPresenter.displayStaticTipsForActivity();
            } else {
                Log.e("TTA", "There are no tips registered for this activity");
            }
        }

        protected static void buildTipsForActivity(Activity activity) {
            int[] identifiers = ResourceUtils.getResourceIdentifiers(activity, tipComposer.tipIdentifiers(activity.getLocalClassName()));
            if (identifiers != null) {
                tipBuilder = new ToolTipBuilder();
                tipBuilder.addStaticTips(activity.getLocalClassName(),
                        identifiers,
                        tipComposer.tipTitles(activity.getLocalClassName()),
                        tipComposer.tipTexts(activity.getLocalClassName()));
            }
        }

        @MainThread
        public static void relaunchHelpForActivity(Activity activity) {
            DataWrapper.resetAcknowledgementForActivity(activity.getLocalClassName());
            buildTipsForActivity(activity);
            showTipsForActivity(activity);
        }

        public static void cleanUp() {
            toolTipPresenter.cleanUp();
        }
    }

    public static class DataWrapper {

        public static boolean resetAcknowledgement(String activityName, int resourceId) {
            return PersistentManager.getInstance().resetAcknowledgement(activityName, resourceId);
        }

        public static boolean isAcknowledged(String activityName, int resourceId) {
            return PersistentManager.getInstance().isAcknowledged(activityName, resourceId);
        }

        public static boolean resetAllAcknowledgements() {
            return PersistentManager.getInstance().resetAllAcknowledgements();
        }

        public static boolean resetAcknowledgementForActivity(String activityName) {
            return PersistentManager.getInstance().resetAcknowledgementForActivity(activityName);
        }
    }
}