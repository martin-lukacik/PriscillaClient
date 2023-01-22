package com.example.priscillaclient.views;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog extends ProgressDialog {

    public LoadingDialog(Context context, String message) {
        super(context);

        setMessage(message);
        setIndeterminate(false);
        setCancelable(true);
    }
}
