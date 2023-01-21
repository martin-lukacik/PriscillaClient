package com.example.priscillaclient.api.user;

import com.example.priscillaclient.api.ApiTask;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.User;

import org.json.JSONObject;

public class GetUserParams extends ApiTask {

    public GetUserParams(HttpResponse context) {
        super(context);
    }

    protected User doInBackground(String... strings) {
        return getUserParams();
    }

    protected User getUserParams() {

        // Use the cached result
        if (client.user != null)
            return client.user;

        try {
            HttpConnection connection = new HttpConnection("/get-full-user-parameters", "GET", false);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorStream());
                return client.user;
            }

            JSONObject response = new JSONObject(connection.getResponse());

            client.user = new User(response);

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return client.user;
    }
}