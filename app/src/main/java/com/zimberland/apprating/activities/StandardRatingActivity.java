package com.zimberland.apprating.activities;


import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zimberland.apprating.R;
import com.zimberland.apprating.utils.DialogFactory;
import com.zimberland.lib.rating.connector.RatingConnector;
import com.zimberland.lib.rating.listener.StandardRatingListener;


public class StandardRatingActivity extends Activity implements StandardRatingListener {
    public static final String TAG = "StandardRatingActivity";
    Button btnValidate;
    RadioGroup radioGroup;

    RatingConnector ratingConnector;
    RatingConnector.LaunchBuilder launchBuilder;
    DialogFactory dialogFactory;
    int launchTimeMax = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standard_activity);
        initializeControls();
        initializeRatingBySession();
        initializeActionBar();
    }

    private void initializeControls() {
        dialogFactory = new DialogFactory(this);

        radioGroup = (RadioGroup) findViewById(R.id.standard_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnValidate.setEnabled(true);
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.four_times:
                        launchTimeMax = 4;
                        break;
                    case R.id.five_times:
                        launchTimeMax = 5;
                        break;
                    case R.id.ten_times:
                        launchTimeMax = 10;
                        break;
                }
            }
        });
        btnValidate = (Button) findViewById(R.id.standard_btn_valide);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingConnector = launchBuilder
                        .setBuilderLaunchNumber(launchTimeMax)
                        .buildStandardRating();
                ratingConnector.doBindStandardRatingService();
            }
        });
    }

    private void initializeRatingBySession() {
        launchBuilder = new RatingConnector.LaunchBuilder(StandardRatingActivity.this)
                .setBuilderStandardListener(StandardRatingActivity.this);
    }

    private void initializeActionBar() {
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Standard Rating");
        }
    }

    public RatingConnector getRatingConnector(){
        return ratingConnector;
    }

    @Override
    public void onStandardRatingMaxLaunchReach() {
        radioGroup.clearCheck();
        btnValidate.setEnabled(false);
        dialogFactory.dismissProgressDialog();
        dialogFactory.displayAlertDialog(getString(R.string.rating_demo_message));
    }

    @Override
    public void onResetStandardRating() {
        Toast.makeText(this, "Standard Rating is reset", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ratingConnector != null)
            ratingConnector.doUnbindService();
    }
}
