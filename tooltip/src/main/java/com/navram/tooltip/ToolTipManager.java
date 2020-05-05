package com.navram.tooltip;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.navram.tooltip.utils.ResourceUtils;

import java.util.List;

/**
 * The entry point to use the library from an app
 * Inject tips and app to the library
 */
public class ToolTipManager {

    private ToolTipManager() {
        //Do nothing
    }

    private static ToolTipBuilder tipBuilder;

    /**
     * Inject the application (to get the context) and the tips via tip composer
     *
     * @param application An application instance for getting the context
     * @param tipComposer All the tips are to be pushed via the {@code ToolTipComposer}
     */
    public static void init(final Application application, final ToolTipComposer tipComposer) {
        PersistentManager.getInstance().init(application);
        ActivityWrapper.init(tipComposer);

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                ActivityWrapper.buildTipsForActivity(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                //Do nothing
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
                //Do nothing
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                //Do nothing
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                //Do nothing
            }
        });
    }

    /**
     * Handles activity related events
     * For example, show, build, relaunch tips for activity
     */
    public static class ActivityWrapper {

        private ActivityWrapper() {
            //Do nothing
        }

        static ToolTipComposer tipComposer;

        static ToolTipPresenter toolTipPresenter;

        /**
         * Inject the tip composer object with all the tip data
         *
         * @param tipComposer a {@link ToolTipComposer} object
         */
        protected static void init(ToolTipComposer tipComposer) {
            ActivityWrapper.tipComposer = tipComposer;
        }

        /**
         * Show the static tips for the given activity
         *
         * @param activity Activity to show the tip
         */
        protected static void showTipsForActivity(Activity activity) {
            if (tipBuilder != null && !tipBuilder.staticTipsForActivity(activity.getLocalClassName()).isEmpty()) {
                toolTipPresenter = new ToolTipPresenter(tipBuilder, activity);
                toolTipPresenter.displayStaticTipsForActivity();
            } else {
                Log.e("TTA", "There are no tips registered for this activity");
            }
        }

        /**
         * Construct the static tips for the given activity from the tipcomposer's tip data
         *
         * @param activity The parent activity
         */
        protected static void buildTipsForActivity(Activity activity) {

            String activityName = activity.getLocalClassName();

            List<ToolTipModel> tipsForActivity = ResourceUtils.findToolTipModelItems(tipComposer.getAllTipData(),
                    activityName);

            if (tipsForActivity.isEmpty()) {
                return;
            }

            tipBuilder = new ToolTipBuilder();

            for (ToolTipModel tipData : tipsForActivity) {

                int resourceIdentifier = ResourceUtils.getResourceId(activity, tipData.getComponentId());

                tipBuilder.addStaticTip(
                        activityName,
                        resourceIdentifier,
                        tipData.getTitle(),
                        tipData.getMessage()
                );
            }
        }

        /**
         * The client app can use this method, if they want to relaunch the tips for any activities
         * This will relaunch all the tips for the given activity, even if the user acknowledged any tips for this activity before.
         * This doesn't affect the acknowledgement of other tips from other activites
         *
         * @param activity The parent activity
         */
        @MainThread
        public static void relaunchHelpForActivity(Activity activity) {
            DataWrapper.resetAcknowledgementForActivity(activity.getLocalClassName());
            buildTipsForActivity(activity);
            showTipsForActivity(activity);
        }

        /**
         * Cleanup stuff
         * Removes the global layout listeners from the presenter
         */
        public static void cleanUp() {
            toolTipPresenter.cleanUp();
        }
    }

    /**
     * Interpret the tip acknowledgement data stored in the persistent storage
     */
    public static class DataWrapper {

        private DataWrapper() {
            //Do nothing
        }

        /**
         * Marks a tip as not acknowledged by the user
         * This removes the entry for the key activityName~resourceId with a boolean
         *
         * @param activityName Local class name of the activity (Can get by calling the getLocalClassName() from the activity instance)
         * @param resourceId   Resource id of the view for which the user has acknowledged the tip
         * @return True, if removing the key in the shared preference is successful, False otherwise
         */
        public static boolean resetAcknowledgement(String activityName, int resourceId) {
            return PersistentManager.getInstance().resetAcknowledgement(activityName, resourceId);
        }

        /**
         * Check if the user is acknowledged the tip before
         *
         * @param activityName Local class name of the activity (Can get by calling the getLocalClassName() from the activity instance)
         * @param resourceId   Resource id of the view for which the user has acknowledged the tip
         * @return True if an entry is found in the shared preferences, False otherwise
         */
        public static boolean isAcknowledged(String activityName, int resourceId) {
            return PersistentManager.getInstance().isAcknowledged(activityName, resourceId);
        }

        /**
         * Reset acknowledgement of all the tips
         *
         * @return True if removing is successful, False otherwise
         */
        public static boolean resetAllAcknowledgements() {
            return PersistentManager.getInstance().resetAllAcknowledgements();
        }

        /**
         * Reset acknowledgement of the tips for a single activity
         * This means, the next time user visits this activity, all the tips for that particular activity will be shown again
         *
         * @param activityName Local class name of the activity (Can get by calling the getLocalClassName() from the activity instance)
         * @return True if removing is successful, False otherwise
         */
        public static boolean resetAcknowledgementForActivity(String activityName) {
            return PersistentManager.getInstance().resetAcknowledgementForActivity(activityName);
        }
    }
}