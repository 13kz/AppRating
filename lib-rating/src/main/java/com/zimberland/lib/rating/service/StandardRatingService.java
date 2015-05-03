package com.zimberland.lib.rating.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zimberland.lib.rating.listener.StandardRatingListener;
import com.zimberland.lib.rating.preferences.RatingPreferences;
import com.zimberland.lib.rating.utils.LocalBinder;
import com.zimberland.lib.rating.utils.RatingConstants;

import java.util.HashSet;
import java.util.Set;

/**
 * Standard Rating Service
 * Used in {@link com.zimberland.lib.rating.connector.RatingConnector}
 *
 * <p>Topics covered here:
 * <ol>
 * <li><a href="#WhatIsAStandardService">What is a Standard Rating Service?</a>
 * <li><a href="#Warning">Warning!</a>
 * </ol>
 *
 * <a name="WhatIsAStandardService"></a>
 * <h3>What is a Standard Rating Service?</h3>
 *
 * <p>A Standard Rating service is a Thread that check the number of time that
 * the method <b>launchStandardRatingProcess</b> is called before indicating the client
 * that max number time is reached (<b>onStandardRatingMaxLaunchReach</b> is called).
 * </p>
 *
 * <a name="Warning"></a>
 * <h3>Warning!</h3>
 * <p><b>DO NOT FORGET</b> Adding the service in AndroidManifest file.</p>
 *
 *
 * @author Mohamed Nazim CAMARA
 */
public class StandardRatingService extends Service {

    /** The local binder representing this Service*/
    private final LocalBinder<StandardRatingService> localBinder =
            new LocalBinder<StandardRatingService>(this);
    /** Declare a Rating preferences to handle the operation of storage or getting data */
    private RatingPreferences ratingPreferences;

    /** The default number of launch max*/
    private long                               launchNumber =
            RatingConstants.DEFAULT_LAUNCH_NUMBER;
    private final Set<StandardRatingListener>  standardRatingListeners =
            new HashSet<StandardRatingListener>();

    @Override
    public void onCreate() {
        super.onCreate();
        ratingPreferences = new RatingPreferences(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * Launching Standard Rating Process.
     * This process is launch only when the max launch
     * is not reach yet.
     */
    public void launchStandardRatingProcess() {
        // Do not start Standard rating process when the status is over
        if(ratingPreferences.isStandardRatingOver()) {
            return;
        }

        long counter = ratingPreferences.getStandardLaunchCounter();
        long launchMax = ratingPreferences.getStandardLaunchMax();
        if(launchMax == RatingConstants.DEFAULT_LAUNCH_NUMBER) {
            ratingPreferences.storeStandardLaunchMax(launchNumber);
        }

        launchMax = ratingPreferences.getStandardLaunchMax();
        // When the number of launch max is not reached
        if(launchMax > counter) {
            // Increment the launch counter
            ratingPreferences.storeStandardLaunchCounter(counter + 1);
        }else {
            // Change Standard Rating status to OVER
            ratingPreferences.storeStandardRatingOver(true);
            // listener callback when max launch is reached
            for(StandardRatingListener standardRatingListener : standardRatingListeners){
                standardRatingListener.onStandardRatingMaxLaunchReach();
            }
        }
    }

    /**
     * Resetting Standard rating process
     * clear standard data
     * launch onReset callback
     */
    public void resetStandardRatingProcess() {
        ratingPreferences.resetStandardRatingData();
        // When the Standard rating is reset
        for(StandardRatingListener standardRatingListener : standardRatingListeners){
            standardRatingListener.onResetStandardRating();
        }
    }

    /**
     * Add Standard rating listener from the rating connector
     * @param listener StandardRatingListener
     */
    public void addStandardRatingListener(StandardRatingListener listener) {
        if(listener != null){
            standardRatingListeners.add(listener);
        }
    }

    /**
     * Remove Standard rating listener from the rating connector
     * @param listener StandardRatingListener
     */
    public void removeStandardRatingListener(StandardRatingListener listener) {
        this.standardRatingListeners.remove(listener);
    }

    /**
     * Change the number of launch max before the rating.
     */
    public void setLaunchNumberMax(long launchNumber) {
        this.launchNumber = launchNumber;
    }
}
