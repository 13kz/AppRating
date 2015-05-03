package com.zimberland.apprating.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zimberland.apprating.R;


public class DemoDialog extends Dialog {

    private LayoutInflater inflater;

    public DemoDialog(final Context context) {
        super(context);
        initDialog(context);
    }

    private void initDialog(Context context) {
        if(context instanceof Activity){
            setOwnerActivity((Activity)context);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        super.setContentView(inflater.inflate(R.layout.demo_dialog, null));
    }

    public void setContentView(final View v, int indexInLayout) {
        setContentView(v, indexInLayout, 0, 0);
    }

    private void setContentView(final View v, int indexInLayout, int marginLeft, int marginRight) {
        final LinearLayout layout = (LinearLayout) findViewById(R.id.contentView);
        int childCount = layout.getChildCount();
        if (indexInLayout < childCount) {
            layout.addView(v, indexInLayout);
        } else {
            layout.addView(v, childCount);
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.rightMargin = marginRight;
        params.leftMargin = marginLeft;
    }

    public void setMessage(final CharSequence message) {
        if (findViewById(R.id.message) == null) {
            final TextView messageView = (TextView) inflater.inflate(R.layout.demo_dialog_message, null);
            messageView.setText(message);
            setContentView(messageView, 1);
        }
    }

    @Override
    public void setTitle(final CharSequence title) {
        final TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText(title);
        titleTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(final int textId) {
        final TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText(textId);
        titleTextView.setVisibility(View.VISIBLE);
    }

    public Button getNegativeButton() {
        return (Button) findViewById(R.id.negativeButton);
    }

    public Button getNeutralButton() {
        return (Button) findViewById(R.id.neutralButton);
    }

    public Button getPositiveButton() {
        return (Button) findViewById(R.id.positiveButton);
    }

    public void setNegativeButton(final int textId, final View.OnClickListener onClickListener) {
        setButton(getNegativeButton(), textId, onClickListener);
    }

    public void setNeutralButton(final int textId, final View.OnClickListener onClickListener) {
        setButton(getNeutralButton(), textId, onClickListener);
    }

    public void setPositiveButton(final int textId, final View.OnClickListener onClickListener) {
        setButton(getPositiveButton(), textId, onClickListener);
    }

    private void setButton(final Button buttonToSet, final int textId, final View.OnClickListener onClickListener) {
        buttonToSet.setText(textId);
        buttonToSet.setOnClickListener(onClickListener);
        buttonToSet.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss() {
        if(getOwnerActivity()!= null && !getOwnerActivity().isFinishing()){
            Log.d(getClass().getSimpleName(), "Dismiss");
            super.dismiss();
        }

    }

    @Override
    public void show() {
        if(getOwnerActivity()!= null && !getOwnerActivity().isFinishing()){
            super.show();
        }
    }
}
