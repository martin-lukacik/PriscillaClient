package com.example.priscillaclient.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.fragments.FragmentBase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public HttpURLConnection getConnection(String endpoint, String method, boolean doOutput) {

        try {
            URL url = new URL(baseUrl + endpoint);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");

            if (client.hasValidToken()) {
                connection.setRequestProperty("Authorization", client.token_type + " " + client.access_token);
            }

            connection.setDoOutput(doOutput);
            connection.setDoInput(true);

            return connection;

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return null;
    }

    protected String getResponse(HttpURLConnection connection) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream responseStream = new BufferedInputStream(connection.getInputStream());
        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
        String line = "";
        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        responseStreamReader.close();

        return stringBuilder.toString();
    }

    protected boolean hasError(HttpURLConnection connection) throws IOException {
        int status = connection.getResponseCode();
        if (status >= 400 && status < 600) {
            logError(connection.getErrorStream());
            return true;
        }
        return false;
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
