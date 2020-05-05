package com.navram.tooltip;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Store and Retrieve the user acknowledgement data of a tip to the persistent storage
 * If an entry is found in the shared preference for a activity name and the resource id, it means that the user has seen/dismissed the tip
 */
public class PersistentManager {

    private static PersistentManager instance;

    private static final Object LOCK = new Object();

    private static final String PREFERENCE_FILE_KEY = "com.navram.tooltip.data";
    private static final String DELIMITER = "~";

    private volatile static SharedPreferences sharedPreferences;

    /**
     * The default constructor is made private to avoid creating this class from outside
     * This is meant to work as a singleton
     */
    private PersistentManager() {
    }

    /**
     * Set the context before using the class.
     * This class needs the context to work with the shared preferences
     * @param context Context Application context
     */
    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    }

    /**
     * Creates (only the first time) and returns a singleton object of this class
     * @return Single instance of this class
     */
    public static PersistentManager getInstance() {

        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new PersistentManager();
                }
            }
        }

        return instance;
    }

    /**
     * Marks a tip as a acknowledged by the user
     * This adds a entry for the key activityName~resourceId with a boolean value True.
     * Once a tip is acknowledged by the user, it will not be displayed to the user until the acknowledgement is reset
     * @param activityName Local class name of the activity (Can get by calling the getLocalClassName() from the activity instance)
     * @param resourceId Resource id of the view for which the user has acknowledged the tip
     * @return True, if saving the key in the shared preference is successful, False otherwise
     */
    public boolean acknowledge(String activityName, int resourceId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(activityName + DELIMITER + resourceId, true);
        return editor.commit();
    }

    /**
     * Marks a tip as not acknowledged by the user
     * This removes the entry for the key activityName~resourceId with a boolean
     * @param activityName Local class name of the activity (Can get by calling the getLocalClassName() from the activity instance)
     * @param resourceId Resource id of the view for which the user has acknowledged the tip
     * @return True, if removing the key in the shared preference is successful, False otherwise
     */
    public boolean resetAcknowledgement(String activityName, int resourceId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(activityName + DELIMITER + resourceId);
        return editor.commit();
    }

    /**
     * Check if the user is acknowledged the tip before
     * @param activityName Local class name of the activity (Can get by calling the getLocalClassName() from the activity instance)
     * @param resourceId Resource id of the view for which the user has acknowledged the tip
     * @return True if an entry is found in the shared preferences, False otherwise
     */
    public boolean isAcknowledged(String activityName, int resourceId) {
        return sharedPreferences.getBoolean(activityName + DELIMITER + resourceId, false);
    }

    /**
     * Reset acknowledgement of all the tips
     * All the entries from the shared preferences gets removed. Clears out the entries completely.
     * @return True if removing is successful, False otherwise
     */
    public boolean resetAllAcknowledgements() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }

    /**
     * Reset acknowledgement of the tips for a single activity
     * This means, all the entries that stars with the activityName will get removed from the shared preferences
     * @param activityName Local class name of the activity (Can get by calling the getLocalClassName() from the activity instance)
     * @return True if removing is successful, False otherwise
     */
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
