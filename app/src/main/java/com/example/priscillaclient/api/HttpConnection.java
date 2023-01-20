package com.example.priscillaclient.api;

import com.example.priscillaclient.models.Client;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpConnection {

    private final HttpURLConnection connection;

    static Client client = Client.getInstance();

    public HttpConnection(String endpoint, String method, boolean doOutput) throws Exception {
        URL url = new URL(ApiTask.baseUrl + endpoint);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");

        if (client.hasValidToken()) {
            connection.setRequestProperty("Authorization", client.token_type + " " + client.access_token);
        }

        connection.setDoOutput(doOutput);
        connection.setDoInput(true);
    }

    public HttpURLConnection getInstance() {
        return connection;
    }

    public void sendRequest(JSONObject json) throws IOException {
        Writer writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
        writer.write(json.toString());
        writer.close();
    }

    public String getResponse() throws IOException {
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

    public InputStream getErrorStream() throws IOException {
        int status = connection.getResponseCode();
        if (status >= 400 && status < 600) {
            return connection.getErrorStream();
        }
        return null;
    }

    public void disconnect() {
        connection.disconnect();
    }

    public OutputStream getOutputStream() throws IOException {
        return connection.getOutputStream();
    }

    public InputStream getInputStream() throws IOException {
        return connection.getInputStream();
    }

    public int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }

    public String getResponseMessage() throws IOException {
        return connection.getResponseMessage();
    }
}
