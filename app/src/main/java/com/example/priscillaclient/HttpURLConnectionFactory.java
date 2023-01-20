package com.example.priscillaclient;

import com.example.priscillaclient.api.ApiTask;
import com.example.priscillaclient.client.Client;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnectionFactory {

    public static HttpURLConnection getConnection(String endpoint, String method, boolean doOutput) {

        try {
            URL url = new URL(ApiTask.baseUrl + endpoint);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");

            if (Client.getInstance().hasValidToken()) {
                String auth = Client.getInstance().token_type + " " + Client.getInstance().access_token;
                connection.setRequestProperty("Authorization", auth);
            }

            if (doOutput) {
                connection.setDoOutput(true);
            }

            connection.setDoInput(true);

            return connection;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
