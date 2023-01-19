package com.example.priscillaclient.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.WindowManager;

import com.example.priscillaclient.LoginActivity;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.client.ClientData;
import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.MainActivity;
import com.example.priscillaclient.models.User;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class RequestToken extends AsyncTask<String, String, User> {

    Context context; // TODO fix the leak || do it outside || null check?

    public RequestToken(Context context) {
        super();

        this.context = context;
    }

    protected User doInBackground(String... strings) {
        return requestToken(strings[0], strings[1], strings[2]);
    }

    protected User requestToken(String username, String password, String email) {

        try {
            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/oauth/token", "POST", true);

            JSONObject json = new JSONObject();
            json.put("client_id", ClientData.client_id);
            json.put("client_secret", ClientData.client_secret);
            json.put("email", email);
            json.put("grant_type", "password");
            json.put("password", password);
            json.put("username", username);

            Log.i("JSON", json.toString());
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(json.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(connection.getResponseCode()));
            Log.i("MSG", connection.getResponseMessage());

            // RESPONSE

            InputStream responseStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            responseStreamReader.close();

            JSONObject response = new JSONObject(stringBuilder.toString());
            Log.i("RESPONSE", response.toString());

            connection.disconnect();

            Client.getInstance().token_type = response.getString("token_type");
            Client.getInstance().expires_in = response.getInt("expires_in");
            Client.getInstance().access_token = response.getString("access_token");
            Client.getInstance().refresh_token = response.getString("refresh_token");
            Client.getInstance().logged_in = System.currentTimeMillis() / 1000;

            return Client.getInstance().user; // TODO why user

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(User user) {
        ((LoginActivity) context).onUpdate(user);
    }
}
