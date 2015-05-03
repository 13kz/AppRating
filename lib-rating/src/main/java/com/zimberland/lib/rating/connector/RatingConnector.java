package com.zimberland.lib.rating.connector;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.zimberland.lib.rating.enums.RatingType;
import com.zimberland.lib.rating.listener.SessionListener;
import com.zimberland.lib.rating.listener.StandardRatingListener;
import com.zimberland.lib.rating.service.RatingBySessionService;
import com.zimberland.lib.rating.service.StandardRatingService;
import com.zimberland.lib.rating.utils.LocalBinder;
import com.zimberland.lib.rating.utils.RatingConstants;

/**
 * @author Mohamed Nazim CAMARA
 *
 * <p>Rating Connector allows clients of the library to create their own
 * Session builder (with the associated listener
 * {@link com.zimberland.lib.rating.listener.SessionListener} and the duration of the session.
 * This information must be in SECONDS) or a Standard Rating builder (with the associated listener
 * {@link com.zimberland.lib.rating.listener.StandardRatingListener} and the number max of launch)
 * depending on the number of application launch before the rating process
 *
 */
public class RatingConnector implements ServiceConnection {

    public static final String TAG = "RatingConnector";

    /** Caller Context*/
    private Context                 context;
    /** The corresponding service bounded */
    private Service                 service;
    /** A flag to check whether or not the service is bounded */
    private boolean                 isServiceBound = false;
    /** The corresponding Rating type. STANDARD is the default one. */
    private RatingType ratingType = RatingType.STANDARD;

    /**
     * Constructs a new empty rating connector
     * @param context The context of the caller
     */
    public RatingConnector(Context context) {
        this.context = context;
    }

    /**
     * Standard Rating Process
     */

    private long launchNumber;
    private StandardRatingListener standardRatingListener;

    /**
     * Builder for the standard rating process
     * Set launch number max and the standard listener
     */
    public static class LaunchBuilder {

        private Context context;
        private long builderLaunchNumber = -1;
        private StandardRatingListener builderStandardListener;

        /**
         * Constructs a new empty Launch Builder
         * @param context The context of the caller
         */
        public LaunchBuilder(Context context) {
            this.context = context;
        }

        /**
         * Change the number of launch before the rating process
         * @param builderLaunchNumber The number of launch
         * @return the launcher builder
         */
        public LaunchBuilder setBuilderLaunchNumber(long builderLaunchNumber) {
            this.builderLaunchNumber = builderLaunchNumber;
            return this;
        }

        /**
         * Change Standard Rating listener
         * @param builderStandardListener the standard rating listener
         * @return the launcher builder
         */
        public LaunchBuilder setBuilderStandardListener(StandardRatingListener builderStandardListener) {
            this.builderStandardListener = builderStandardListener;
            return this;
        }

        /**
         * Build and return the Rating connector object with the required
         * Standard Rating data
         */
        public RatingConnector buildStandardRating() {
            if(hasRequireStandardData()){
                throw new IllegalArgumentException("No standard rating " +
                        "listener set or launch number initialize");
            }
            RatingConnector ratingConnector = new RatingConnector(context);
            ratingConnector.setLaunchNumber(builderLaunchNumber);
            ratingConnector.setStandardRatingListener(builderStandardListener);
            ratingConnector.setRatingType(RatingType.STANDARD);
            return ratingConnector;
        }

        /**
         * Check whether or not all data require for
         * the standard rating process are available
         * @return
         */
        private boolean hasRequireStandardData() {
            if(builderLaunchNumber != -1 || builderStandardListener == null){
                return false;
            }
            return true;
        }
    }

    /**
     * Bind Standard Rating service
     */
    public void doBindStandardRatingService() {
        Intent intent = new Intent(context, StandardRatingService.class);
        context.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    public void setLaunchNumber(long launchNumber) {
        this.launchNumber = launchNumber;
    }

    public void setStandardRatingListener(StandardRatingListener standardRatingListener) {
        this.standardRatingListener = standardRatingListener;
    }

    /**
     * Resetting all standard rating data
     */
    public void resetStandardRating() {
        if(ratingType == RatingType.STANDARD) {
            ((StandardRatingService) service).resetStandardRatingProcess();
        }
    }

    /**
     * Rating process by a specified session
     */

    private long duration;
    private SessionListener sessionListener;

    /**
     * Builder for the session rating process
     * Set the session duration and the session listener
     */
    public static class SessionBuilder {

        private Context             context;
        // Session Duration in SECONDS;
        private long                builderSessionDuration = RatingConstants.DEFAULT_SESSION_DURATION;
        private SessionListener     builderSessionListener;

        /**
         * Constructs a new empty Session Builder
         * @param context The context of the caller
         */
        public SessionBuilder(Context context) {
            this.context = context;
        }

        /**
         * Change Session duration
         * @param duration : The duration of the session in SECONDS
         */
        public SessionBuilder setBuilderDuration(long duration){
            this.builderSessionDuration = duration;
            return this;
        }

        /**
         * Change the session listener
         */
        public SessionBuilder setBuilderSessionListener(SessionListener builderSessionLister) {
            this.builderSessionListener = builderSessionLister;
            return this;
        }

        /**
         * Build and return the Rating connector object with the required
         * Session Rating data
         */
        public RatingConnector buildSession(){
            if(hasRequireSessionData()){
                throw new IllegalArgumentException("No session listener set or session duration initialize");
            }
            RatingConnector ratingConnector = new RatingConnector(context);
            ratingConnector.setSessionDuration(builderSessionDuration);
            ratingConnector.setSessionListener(builderSessionListener);
            ratingConnector.setRatingType(RatingType.SESSION);
            return ratingConnector;
        }

        /**
         * Check whether or not all data require for
         * the session rating process are available
         * return True if all require are set
         */
        private boolean hasRequireSessionData() {
            if(builderSessionDuration != -1 || builderSessionListener == null){
                return false;
            }
            return true;
        }
    }

    /**
     * Bind Session Rating service.
     * Using BIND_AUTO_CREATE as operation options for the binding.
     */
    public void doBindRatingBySessionService() {
        Intent intent = new Intent(context, RatingBySessionService.class);
        context.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    /**
     * Change Session Duration.
     */
    public void setSessionDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Change Session Listener.
     */
    public void setSessionListener(SessionListener sessionListener) {
        this.sessionListener = sessionListener;
    }

    /**
     * Resetting Session rating process data.
     */
    public void resetRatingSession(){
        if(ratingType == RatingType.SESSION) {
            ((RatingBySessionService) service).resetRatingProcess();
        }
    }

    /**
     * Disconnect corresponding Service.
     * Depends on the rating type set.
     */
    public void doUnbindService() {
        if (isServiceBound) {
            context.unbindService(this);
            isServiceBound = false;
        }

        if(service != null){
            // Rating By session
            if(ratingType == RatingType.SESSION) {
                ((RatingBySessionService) service).removeSessionListener(sessionListener);
            }

            // Standard Rating (Number of app launch)
            if(ratingType == RatingType.STANDARD) {
                ((StandardRatingService) service).removeStandardRatingListener(standardRatingListener);
            }

        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        LocalBinder<Service> castedBinder = (LocalBinder<Service>) iBinder;
        service = castedBinder.getService();

        // Rating By session
        if(ratingType == RatingType.SESSION) {
            ((RatingBySessionService) service).addSessionListener(sessionListener);
            ((RatingBySessionService) service).setSessionDuration(duration);
            ((RatingBySessionService) service).startSessionRatingProcess();
        }

        // Standard Rating (Number of app launch)
        if(ratingType == RatingType.STANDARD) {
            ((StandardRatingService) service).addStandardRatingListener(standardRatingListener);
            ((StandardRatingService) service).setLaunchNumberMax(launchNumber);
            ((StandardRatingService) service).launchStandardRatingProcess();
        }

        isServiceBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        isServiceBound = false;
    }

    /**
     * Change the rating type.
     */
    public void setRatingType(RatingType ratingType) {
        this.ratingType = ratingType;
    }

    /**
     * Return the set rating type.
     */
    public RatingType getRatingType() {
        return ratingType;
    }

    public long getLaunchNumber() {
        return launchNumber;
    }

    public StandardRatingListener getStandardRatingListener() {
        return standardRatingListener;
    }

    public long getDuration() {
        return duration;
    }
}
