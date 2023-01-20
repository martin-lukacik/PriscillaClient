package com.example.priscillaclient.api;

import com.example.priscillaclient.api.client.Client;

import org.json.JSONException;
import org.json.JSONObject;

public class GetToken extends ApiTask {

    public GetToken(HttpResponse context) {
        super(context);
    }

    protected Client doInBackground(String... strings) {
        return requestToken(strings[0], strings[1], strings[2], strings[3]);
    }

    protected Client requestToken(String username, String password, String email, String grant_type) {

        try {
            HttpConnection connection = new HttpConnection("/oauth/token", "POST", true);

            JSONObject json = getJsonObject(username, password, email, grant_type);

            connection.sendRequest(json);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorStream());
                return Client.getInstance();
            }

            JSONObject response = new JSONObject(connection.getResponse());

            Client.set(response);

            connection.disconnect();

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return Client.getInstance();
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
