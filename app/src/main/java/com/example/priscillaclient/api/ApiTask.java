package com.example.priscillaclient.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.priscillaclient.api.client.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public abstract class ApiTask extends AsyncTask<String, String, Object> {

    public static final String baseUrl = "https://app.priscilla.fitped.eu";
    public static final int client_id = 2;
    public static final String client_secret = "iQuGUAzqc187j7IKQ94tTVJAywHCAzYBGAMTxEtr";

    protected final static Client client = Client.getInstance();

    final HttpResponse fragment;

    public String errorMessage = null;

    public ApiTask(HttpResponse fragment) {
        super();
        this.fragment = fragment;
    }

    protected void onPostExecute(Object response) {

        Activity activity = (fragment instanceof Activity ? (Activity) fragment : ((Fragment) fragment).getActivity());
        if (errorMessage != null) {
            Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
        }

        fragment.onUpdate(response);
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
