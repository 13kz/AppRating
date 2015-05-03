package com.zimberland.lib.rating.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.zimberland.lib.rating.listener.SessionListener;
import com.zimberland.lib.rating.preferences.RatingPreferences;
import com.zimberland.lib.rating.utils.LocalBinder;
import com.zimberland.lib.rating.utils.RatingConstants;

import java.util.HashSet;
import java.util.Set;

/**
 * Session Rating Service
 * Used in {@link com.zimberland.lib.rating.connector.RatingConnector}
 *
 * <p>Topics covered here:
 * <ol>
 * <li><a href="#WhatIsASessionRatingService">What is a Session Rating Service?</a>
 * <li><a href="#Warning">Warning!</a>
 * </ol>
 *
 * <a name="WhatIsASessionRatingService"></a>
 * <h3>What is a Session Rating Service?</h3>
 *
 * <p>A Session Rating service is a Thread that detect whether or not
 * the created session is over (<b>onSessionDurationEnd</b> is called).
 * </p>
 *
 * <a name="Warning"></a>
 * <h3>Warning!</h3>
 * <p><b>DO NOT FORGET</b> Adding the service in AndroidManifest file.</p>
 *
 * @author Mohamed Nazim CAMARA
 */
public class RatingBySessionService extends Service {

    /** The local binder representing this Service*/
    private final LocalBinder<RatingBySessionService> localBinder =
            new LocalBinder<RatingBySessionService>(this);
    /** A Handler to post delay the end of the session using the duration of it or
     *  or the reminded time*/
    private Handler                                         senderHandle = new Handler();
    /** Declare a Rating preferences to handle the operation of storage or getting data */
    private RatingPreferences ratingPreferences;

    private long                                            sessionDuration =
            RatingConstants.DEFAULT_SESSION_DURATION;
    private final Set<SessionListener>                      sessionListeners =
            new HashSet<SessionListener>();

    /**
     * Session process runnable which execute the following operations :
     * Change session rating status, launch onSessionDurationEnd callback, remove session handler.
     */
    private Runnable sessionProcessRunnable = new Runnable() {
        @Override
        public void run() {
            ratingPreferences.storeEndOfSession(true);
            // When the session duration is over
            for(SessionListener sessionListener : sessionListeners){
                sessionListener.onSessionDurationEnd();
            }
            removeSessionHandler();
        }
    };

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
     * Start Session rating process.
     * This process is launch only if the session
     * is not over yet.
     */
    public void startSessionRatingProcess() {
        // Do not start Session rating process when the status is over
        if(ratingPreferences.isSessionOver()){
            return;
        }

        long duration = ratingPreferences.getStartSessionTime();
        if(duration == -1) {
            ratingPreferences.storeSessionDuration(sessionDuration * 1000);
            ratingPreferences.storeStartSessionTime(System.currentTimeMillis());
        }
        startSessionScheduler();
    }

    /**
     * Starting Session scheduler.
     * Check the reminding before the end of the session.
     */
    private void startSessionScheduler() {
        if(senderHandle == null){
            senderHandle = new Handler();
        }

        long startSessionTime = ratingPreferences.getStartSessionTime();
        long currentTime = System.currentTimeMillis();
        long duration = ratingPreferences.getSessionDuration();

        // Check whether or not session is over
        if(currentTime - startSessionTime >= duration){
            // When the session duration is over
            for(SessionListener sessionListener : sessionListeners){
                sessionListener.onSessionDurationEnd();
            }
            removeSessionHandler();
        }else{
            // Post delay the session process runnable
            // the delay represents the remind time
            long remindTime = duration - (currentTime - startSessionTime);
            senderHandle.postDelayed(sessionProcessRunnable, remindTime);
        }
    }

    /**
     * Stop Session.
     * The operation includes resetting session rating data, removing session handler.
     */
    public void stopAndResetSessionRatingProcess() {
        resetRatingProcess();
        removeSessionHandler();
    }


    /**
     * Remove session process runnable
     * from the handler.
     */
    public void removeSessionHandler(){
        if(senderHandle != null && sessionProcessRunnable != null){
            senderHandle.removeCallbacks(sessionProcessRunnable);
        }
    }

    /**
     * Reset all session rating data.
     * And call <b>onSessionDurationReset</b> to inform
     * the client that the rating process is reset.
     */
    public void resetRatingProcess(){
        ratingPreferences.resetSessionData();
        // When the session is reset
        for(SessionListener sessionListener : sessionListeners){
            sessionListener.onSessionDurationReset();
        }
    }

    /**
     * Add Session rating listener from the rating connector
     * {@link com.zimberland.lib.rating.connector.RatingConnector}
     * @param listener SessionListener
     */
    public void addSessionListener(SessionListener listener) {
        if(listener != null){
            sessionListeners.add(listener);
        }
    }

    /**
     * Remove Session rating listener from the rating connector
     * {@link com.zimberland.lib.rating.connector.RatingConnector}
     * @param listener SessionListener
     */
    public void removeSessionListener(SessionListener listener) {
        this.sessionListeners.remove(listener);
    }

    /**
     * Change session duration.
     */
    public void setSessionDuration(long sessionDuration) {
        this.sessionDuration = sessionDuration;
    }
}
