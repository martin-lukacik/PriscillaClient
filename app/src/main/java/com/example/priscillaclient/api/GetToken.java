package com.example.priscillaclient.api;

import com.example.priscillaclient.client.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

public class GetToken extends ApiTask {

    public GetToken(HttpResponse context) {
        super(context);
    }

    protected Client doInBackground(String... strings) {
        return requestToken(strings[0], strings[1], strings[2], strings[3]);
    }

    protected Client requestToken(String username, String password, String email, String grant_type) {

        try {
            HttpURLConnection connection = getConnection("/oauth/token", "POST", true);

            JSONObject json = getJsonObject(username, password, email, grant_type);

            sendRequest(connection, json);

            if (hasError(connection))
                return Client.getInstance();

            JSONObject response = new JSONObject(getResponse(connection));

            Client.set(response);

            connection.disconnect();

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return Client.getInstance();
    }

    private void sendRequest(HttpURLConnection connection, JSONObject json) throws IOException {
        DataOutputStream os = new DataOutputStream(connection.getOutputStream());
        os.writeBytes(json.toString());
        os.flush();
        os.close();
    }

    private JSONObject getJsonObject(String username, String password, String email, String grant_type) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("client_id", client_id);
        json.put("client_secret", client_secret);
        json.put("email", email);
        json.put("grant_type", grant_type);
        json.put(grant_type, password);
        json.put("username", username);
        return json;
    }
}
