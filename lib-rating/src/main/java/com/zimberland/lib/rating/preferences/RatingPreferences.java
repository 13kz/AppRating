package com.zimberland.lib.rating.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.zimberland.lib.rating.utils.RatingConstants;


/**
 * Rating Preferences
 * Storing and getting session rating data
 * Storing and getting standard rating data
 */
public class RatingPreferences {

    public static final String PREFERENCE_NAME = "LOCAL_PREFERENCE";

    // Session rating keys
    public static final String SESSION_DURATION_KEY = "session_duration";
    public static final String START_SESSION_TIME_KEY = "last_known_time";
    public static final String SESSION_OVER_KEY = "session_over";

    // Standard Rating Keys
    public static final String STANDARD_LAUNCH_MAX_KEY = "number_launch_max";
    public static final String STANDARD_LAUNCH_COUNTER_KEY = "launch_counter";
    public static final String STANDARD_LAUNCH_OVER_KEY = "launch_over";

    private SharedPreferences sharedPreferences;
    private static RatingPreferences instance;
    private Context context;

    /**
     * Get the instance of the rating preferences
     * @param context
     * @return rating preferences instance
     */
    public static RatingPreferences getInstance(Context context) {
        if (instance == null) {
            synchronized (RatingPreferences.class) {
                if (instance == null) {
                    instance = new RatingPreferences(context);
                }
            }
        }
        return instance;
    }

    public RatingPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Session Rating process preferences
     */


    /**
     * Store the session duration
     * @param duration : session duration
     */
    public void storeSessionDuration(long duration) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(SESSION_DURATION_KEY, duration);
        editor.commit();
    }

    /**
     * Get the duration of the current session
     * @return session duration
     */
    public long getSessionDuration() {
        return sharedPreferences.getLong(SESSION_DURATION_KEY, RatingConstants.DEFAULT_SESSION_DURATION);
    }

    /**
     * Store the debut of the current session
     * @param startSessionTime session debut time
     */
    public void storeStartSessionTime(long startSessionTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(START_SESSION_TIME_KEY, startSessionTime);
        editor.commit();
    }

    /**
     * Get the time when the session starts
     * @return session start time
     */
    public long getStartSessionTime() {
        return sharedPreferences.getLong(START_SESSION_TIME_KEY, -1);
    }

    /**
     * Indicate that the session is over
     * @param isOver
     */
    public void storeEndOfSession(boolean isOver) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SESSION_OVER_KEY, isOver);
        editor.commit();
    }

    /**
     * Get if the session is over or not
     * @return  true when the session is over
     *          false if not
     */
    public boolean isSessionOver() {
        return sharedPreferences.getBoolean(SESSION_OVER_KEY, false);
    }

    /**
     * Reset all session data
     * - Session duration
     * - Session debut time
     * - Session status
     */
    public void resetSessionData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SESSION_DURATION_KEY);
        editor.remove(START_SESSION_TIME_KEY);
        editor.remove(SESSION_OVER_KEY);
        editor.commit();
    }

    /**
     * Standard Rating process preferences
     */

    /**
     * Store the number max of times before the rating
     * @param launchMax : Max launch times
     */
    public void storeStandardLaunchMax(long launchMax) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(STANDARD_LAUNCH_MAX_KEY, launchMax);
        editor.commit();
    }

    /**
     * Get the number of launch times max before the rating
     * @return Max launch times
     */
    public long getStandardLaunchMax() {
        return sharedPreferences.getLong(STANDARD_LAUNCH_MAX_KEY, -1);
    }

    /**
     * Count the current launch compare to the start
     * @param counter
     */
    public void storeStandardLaunchCounter(long counter) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(STANDARD_LAUNCH_COUNTER_KEY, counter);
        editor.commit();
    }

    /**
     * Get the current launch number
     * @return current launch number
     */
    public long getStandardLaunchCounter() {
        return sharedPreferences.getLong(STANDARD_LAUNCH_COUNTER_KEY, 1);
    }

    /**
     * Standard rating process status
     * @param isOver
     */
    public void storeStandardRatingOver(boolean isOver) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(STANDARD_LAUNCH_OVER_KEY, isOver);
        editor.commit();
    }

    /**
     * Get the standard rating status
     * @return true if the rating is already proposed
     *         false if not yet
     */
    public boolean isStandardRatingOver() {
        return sharedPreferences.getBoolean(STANDARD_LAUNCH_OVER_KEY, false);
    }

    /**
     * Reset all standard rating data
     * - Number of launch max
     * - Launch counter
     * - Standard rating status
     */
    public void resetStandardRatingData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(STANDARD_LAUNCH_MAX_KEY);
        editor.remove(STANDARD_LAUNCH_COUNTER_KEY);
        editor.remove(STANDARD_LAUNCH_OVER_KEY);
        editor.commit();
    }

}
