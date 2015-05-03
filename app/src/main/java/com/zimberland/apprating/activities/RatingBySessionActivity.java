package com.zimberland.apprating.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zimberland.apprating.R;
import com.zimberland.apprating.utils.DialogFactory;
import com.zimberland.lib.rating.connector.RatingConnector;
import com.zimberland.lib.rating.listener.SessionListener;

public class RatingBySessionActivity extends Activity implements SessionListener {

    public static final String TAG = "RatingBySessionActivity";
    TextView txtTitle;
    EditText edtNumber;
    Button btnValidate;

    RatingConnector ratingConnector;
    RatingConnector.SessionBuilder sessionBuilder;
    DialogFactory dialogFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_activity);
        initializeActionBar();
        initializeControls();
        initializeRatingBySession();
    }

    private void initializeActionBar() {
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Session Rating");
        }
    }

    private void initializeControls() {
        dialogFactory = new DialogFactory(this);

        txtTitle = (TextView) findViewById(R.id.txt_title);
        edtNumber = (EditText) findViewById(R.id.edt_number);
        btnValidate = (Button) findViewById(R.id.btn_valide);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtNumber.getText().toString())) {
                    Toast.makeText(RatingBySessionActivity.this, "Empty Field", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        long duration = (long) Integer.parseInt(edtNumber.getText().toString());
                        Toast.makeText(RatingBySessionActivity.this, "Rating by session started", Toast.LENGTH_LONG).show();
                        ratingConnector = sessionBuilder
                                .setBuilderDuration(duration)
                                .buildSession();
                        ratingConnector.doBindRatingBySessionService();
                        dialogFactory.showProgressDialog("Session in progress ...");
                    } catch (Exception e) {
                        Toast.makeText(RatingBySessionActivity.this, "Not the correct format", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void initializeRatingBySession() {
        sessionBuilder = new RatingConnector.SessionBuilder(RatingBySessionActivity.this)
                .setBuilderSessionListener(RatingBySessionActivity.this);
    }

    public RatingConnector getRatingConnector(){
        return ratingConnector;
    }

    @Override
    public void onSessionDurationEnd() {
        dialogFactory.dismissProgressDialog();
        dialogFactory.displayAlertDialog(getString(R.string.rating_demo_message));
    }

    @Override
    public void onSessionDurationReset() {
        Toast.makeText(this, "Session is reset", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ratingConnector != null)
            ratingConnector.doUnbindService();
    }
}
