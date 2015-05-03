package com.zimberland.apprating.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import com.zimberland.apprating.R;
import com.zimberland.apprating.utils.IntentHelper;
import com.zimberland.apprating.utils.RatingUtils;

import java.util.List;


public class DemoActivity extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    List<String> allTypes;
    IntentHelper intentHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        intentHelper = new IntentHelper(this);
        initializeControls();
    }

    private void initializeControls() {
        spinner = (Spinner) findViewById(R.id.rating_type);
        allTypes = RatingUtils.getRatingTypes(getString(R.string.spinner_prompt));
        ArrayAdapter<String> ratingTypes = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                allTypes);
        spinner.setAdapter(ratingTypes);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
                intentHelper.startStandardRatingActivity();
                break;
            case 2:
                intentHelper.startSessionRatingActivity();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
