package com.zimberland.lib.rating.listener;

/**
 * Session rating process listener
 *
 * {@link com.zimberland.lib.rating.service.RatingBySessionService}
 *
 * Monitoring the state of a session rating process service
 * <p>Like many callbacks from the system, the methods on this class are called
 * from the main thread of your process.
 */
public interface SessionListener {

    /**
     * Called when created session is over
     */
    public void onSessionDurationEnd();

    /**
     * Called when session is reset
     */
    public void onSessionDurationReset();
}
