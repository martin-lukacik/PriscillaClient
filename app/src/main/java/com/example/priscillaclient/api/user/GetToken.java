package com.example.priscillaclient.api.user;

import com.example.priscillaclient.api.ApiTask;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.api.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class GetToken extends ApiTask {

    public GetToken(HttpResponse context) {
        super(context);
    }

    protected Client doInBackground(String... strings) {
        String username = strings[0];
        String password = strings[1];
        String email = strings[2];
        String grant_type = strings[3];

        try {
            HttpConnection connection = new HttpConnection("/oauth/token", "POST", true);

            JSONObject json = getJsonObject(username, password, email, grant_type);

            connection.sendRequest(json);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorStream());
                return client;
            }

            JSONObject response = new JSONObject(connection.getResponse());

            Client.set(response);

            connection.disconnect();

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return client;
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
