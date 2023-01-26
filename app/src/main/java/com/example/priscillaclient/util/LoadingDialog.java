package com.example.priscillaclient.util;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog extends ProgressDialog {

    public LoadingDialog(Context context) {
        super(context);

        setMessage("Loading, please wait...");
        setIndeterminate(false);
        setCancelable(false);
    }
}
