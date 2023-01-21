package com.example.priscillaclient.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.priscillaclient.LoginActivity;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.views.LoadingDialog;

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

    LoadingDialog dialog;

    public ApiTask(HttpResponse context) {
        super();
        this.context = context;

        Activity ctx = (context instanceof Activity ? (Activity) context : ((Fragment) context).getActivity());
        dialog = new LoadingDialog(ctx, "Loading, please wait...");
        dialog.show();
    }

    protected void onPostExecute(Object response) {

        if (dialog != null)
            dialog.dismiss();

        Activity activity = (context instanceof Activity ? (Activity) context : ((Fragment) context).getActivity());
        if (errorMessage != null) {
            Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
        }

        context.onUpdate(response);
    }

    protected void logError(String message) {
        errorMessage = message;
    }

    protected void logError(InputStream is) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            br.close();
            is.close();

            errorMessage = stringBuilder.toString();
        } catch (Exception ignore) {

        }
    }
}
