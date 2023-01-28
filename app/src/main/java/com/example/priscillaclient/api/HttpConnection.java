package com.example.priscillaclient.api;

import com.example.priscillaclient.viewmodels.user.models.Token;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpConnection {

    private final HttpURLConnection connection;

    private static final String baseUrl = "https://app.priscilla.fitped.eu";

    public HttpConnection(String endPoint, String method) throws Exception {
        URL url = new URL(baseUrl + endPoint);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");

        if (Token.isValid()) {
            connection.setRequestProperty("Authorization", Token.get().token_type + " " + Token.get().access_token);
        }

        connection.setDoOutput(method.equals("POST"));
        connection.setDoInput(true);
    }

    public void sendRequest(JSONObject json) throws IOException {
        Writer writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
        writer.write(json.toString());
        writer.close();
    }

    public String getResponse() throws IOException {
        int status = connection.getResponseCode();
        if (status >= 400 && status < 600) {
            return connection.getResponseMessage();
        }

        StringBuilder stringBuilder = new StringBuilder();
        InputStream responseStream = new BufferedInputStream(connection.getInputStream());
        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

        String line;
        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        responseStreamReader.close();

        return stringBuilder.toString();
    }

    public InputStream getErrorStream() throws IOException {
        return connection.getErrorStream();
    }

    public String getErrorMessage() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(getErrorStream()));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }
        br.close();
        return stringBuilder.toString();
    }

    public void disconnect() {
        connection.disconnect();
    }

    public InputStream getInputStream() throws IOException {
        return connection.getInputStream();
    }

    public int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }
}
