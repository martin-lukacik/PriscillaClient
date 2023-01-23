package com.example.priscillaclient.api;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.priscillaclient.LoginActivity;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.views.LoadingDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class ApiTask extends AsyncTask<String, String, Object> {

    public static final String baseUrl = "https://app.priscilla.fitped.eu";
    public static final int client_id = 2;
    public static final String client_secret = "iQuGUAzqc187j7IKQ94tTVJAywHCAzYBGAMTxEtr";

    protected final static Client client = Client.getInstance();

    final HttpResponse context;

    public String errorMessage = null;

    protected LoadingDialog dialog;

    public ApiTask(HttpResponse context) {
        super();
        this.context = context;
        showProgressDialog(true);
    }

    protected void onPostExecute(Object response) {

        if (dialog != null) {
            Activity activity = (context instanceof Activity ? (Activity) context : ((Fragment) context).getActivity());
            if (activity != null)
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            try {
                dialog.dismiss();
            } catch (Exception ignore) { }
        }

        Activity activity = (context instanceof Activity ? (Activity) context : ((Fragment) context).getActivity());
        if (errorMessage != null) {
            if (errorMessage.equals("Unauthorized.") && activity != null) {
                activity.startActivity(new Intent(activity, LoginActivity.class));
            } else {
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
            }
        }

        if (activity == null || activity.isDestroyed()) {
            return;
        }

        context.onUpdate(response);
    }

    public void showProgressDialog(boolean dismissable) {
        Activity activity = (context instanceof Activity ? (Activity) context : ((Fragment) context).getActivity());

        if (!dismissable && activity != null)
            activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            );

        dialog = new LoadingDialog(activity, "Loading, please wait...");
        dialog.show();
    }

    protected void logError(String message) {
        try {
            JSONObject response = new JSONObject(message);
            errorMessage = response.getString("message");
        } catch (Exception e) {
            errorMessage = message;
        }
    }
}
