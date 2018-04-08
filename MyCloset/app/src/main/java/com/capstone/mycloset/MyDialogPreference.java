package com.capstone.mycloset;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class MyDialogPreference extends DialogPreference {
    public MyDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {

        }
        else if(which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.cancel();
        }
    }
}
