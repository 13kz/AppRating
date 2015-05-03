package com.zimberland.lib.rating.listener;

/**
 * Standard rating process listener
 *
 * {@link com.zimberland.lib.rating.service.StandardRatingService}
 *
 * Monitoring the state of a rating process service
 * <p>Like many callbacks from the system, the methods on this class are called
 * from the main thread of your process.
 */
public interface StandardRatingListener {

    /**
     * Called when number of launch max is reach
     */
    public void onStandardRatingMaxLaunchReach();

    /**
     * Called when Standard rating process is reset
     */
    public void onResetStandardRating();
}
