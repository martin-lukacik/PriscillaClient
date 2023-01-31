package com.example.priscillaclient.misc;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.priscillaclient.R;

public class LoadingDialog extends ProgressDialog {

    public LoadingDialog(Context context) {
        super(context);

        setMessage(context.getString(R.string.loading));
        setIndeterminate(false);
        setCancelable(false);
    }
}
