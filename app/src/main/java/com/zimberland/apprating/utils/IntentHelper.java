package com.zimberland.apprating.utils;

import android.content.Context;
import android.content.Intent;

import com.zimberland.apprating.activities.DemoActivity;
import com.zimberland.apprating.activities.RatingBySessionActivity;
import com.zimberland.apprating.activities.StandardRatingActivity;


/**
 * Initialize Intent and start activities
 */
public class IntentHelper {

    Context context;

    public IntentHelper(Context context) {
        this.context = context;
    }

    public void startDemoActivity() {
        Intent intent = new Intent(context, DemoActivity.class);
        context.startActivity(intent);
    }

    public void startStandardRatingActivity() {
        Intent intent = new Intent(context, StandardRatingActivity.class);
        context.startActivity(intent);
    }

    public void startSessionRatingActivity() {
        Intent intent = new Intent(context, RatingBySessionActivity.class);
        context.startActivity(intent);
    }
}
