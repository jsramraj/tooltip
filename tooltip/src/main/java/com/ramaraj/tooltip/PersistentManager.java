package com.ramaraj.tooltip;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Store and Retrieve the user acknowledgement data of a tip to the persistent storage
 */
public class PersistentManager {
    private static PersistentManager instance;
    private static final Object lock = new Object();

    private static final String preferenceFileKey = "com.ramaraj.tooltip.data";
    private static final String delimiter = "~";
    private static SharedPreferences sharedPreferences;

    private PersistentManager() {
    }

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(preferenceFileKey, Context.MODE_PRIVATE);
    }
    
    public static PersistentManager getInstance() {

        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new PersistentManager();
                }
            }
        }

        return instance;
    }

    public boolean acknowledge(String activityName, int resourceId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(activityName + delimiter + resourceId, true);
        return editor.commit();
    }

    public boolean resetAcknowledgement(String activityName, int resourceId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(activityName + delimiter + resourceId);
        return editor.commit();
    }

    public boolean isAcknowledged(String activityName, int resourceId) {
        return sharedPreferences.getBoolean(activityName + delimiter + resourceId, false);
    }

    public boolean resetAllAcknowledgements() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }

    public boolean resetAcknowledgementForActivity(String activityName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (String key : sharedPreferences.getAll().keySet()) {
            if (key.startsWith(activityName)) {
                editor.remove(key);
            }
        }

        return editor.commit();
    }
}
