package com.example.priscillaclient.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.LoginActivity;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.client.ClientData;
import com.example.priscillaclient.models.User;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class RequestToken extends ApiTask {

    public RequestToken(HttpResponse context) {
        super(context);
    }

    protected Client doInBackground(String... strings) {
        return requestToken(strings[0], strings[1], strings[2], strings[3]);
    }

    protected Client requestToken(String username, String password, String email, String grant_type) {

        try {
            HttpURLConnection connection = getConnection("/oauth/token", "POST", true);

            JSONObject json = new JSONObject();
            json.put("client_id", ClientData.client_id);
            json.put("client_secret", ClientData.client_secret);
            json.put("email", email);
            json.put("grant_type", grant_type);
            json.put(grant_type, password);
            json.put("username", username);

            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(json.toString());

            os.flush();
            os.close();

            int status = connection.getResponseCode();
            if (status >= 400 && status < 600) {
                logError(connection.getErrorStream());
                return Client.getInstance();
            }

            InputStream responseStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            responseStreamReader.close();
            connection.disconnect();

            JSONObject response = new JSONObject(stringBuilder.toString());

            Client client = Client.getInstance();
            client.token_type = response.getString("token_type");
            client.expires_in = response.getInt("expires_in");
            client.access_token = response.getString("access_token");
            client.refresh_token = response.getString("refresh_token");
            client.logged_in = System.currentTimeMillis() / 1000;

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return Client.getInstance();
    }
}
