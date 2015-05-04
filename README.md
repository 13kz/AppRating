# AppRating
Offer the possibility to the user to rate the application after a session or a number of launch.

###Rating by SESSION

Give to the user the possibility to display a dialog after a session.

- Set the session duration in SECONDS.
- Set the [SessionListener] to have the callback onSessionDurationEnd (at the end of the session) and onSessionDurationReset

###Standard Rating

Give to the user the possibility to display a dialog after a number of app launch. 

- Set the number max of launch.
- Set the [StandardRatingListener] to have the callback onStandardRatingMaxLaunchReach and onResetStandardRating

###DEMO
[Lib Demo] show how to use the library. 

[SessionListener]:https://github.com/13kz/AppRating/blob/master/lib-rating/src/main/java/com/zimberland/lib/rating/listener/SessionListener.java
[StandardRatingListener]:https://github.com/13kz/AppRating/blob/master/lib-rating/src/main/java/com/zimberland/lib/rating/listener/StandardRatingListener.java
[Lib Demo]:https://github.com/13kz/AppRating/tree/master/app

