package com.zimberland.apprating.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.zimberland.apprating.R;
import com.zimberland.apprating.activities.RatingBySessionActivity;
import com.zimberland.apprating.activities.StandardRatingActivity;
import com.zimberland.apprating.widget.DemoDialog;
import com.zimberland.lib.rating.connector.RatingConnector;


public class DialogFactory {

    private Context context;
    private ProgressDialog progressDialog;
    private IntentHelper intentHelper;

    public DialogFactory(Context context) {
        this.context = context;
        intentHelper = new IntentHelper(context);
    }

    public void displayAlertDialog(String message) {

        final DemoDialog demoAlertDialog = new DemoDialog(context);

        demoAlertDialog.setMessage(message);
        demoAlertDialog.setCancelable(false);

        demoAlertDialog.setPositiveButton(R.string.yes, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "TODO : Open google play store", Toast.LENGTH_LONG).show();
                demoAlertDialog.dismiss();
                intentHelper.startDemoActivity();

            }
        });

        demoAlertDialog.setNeutralButton(R.string.later, new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if(context instanceof RatingBySessionActivity){
                    RatingConnector ratingConnector =
                            ((RatingBySessionActivity) context).getRatingConnector();
                    ratingConnector.resetRatingSession();
                }
                if(context instanceof StandardRatingActivity){
                    RatingConnector ratingConnector =
                            ((StandardRatingActivity) context).getRatingConnector();
                    ratingConnector.resetStandardRating();
                }
                demoAlertDialog.dismiss();
            }
        });

        demoAlertDialog.setNegativeButton(R.string.no, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Thanks", Toast.LENGTH_LONG).show();
                demoAlertDialog.dismiss();
                intentHelper.startDemoActivity();
            }
        });
        demoAlertDialog.show();
    }

    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
