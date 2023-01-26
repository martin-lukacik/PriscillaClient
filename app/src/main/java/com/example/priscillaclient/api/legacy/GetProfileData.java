package com.example.priscillaclient.api.legacy;

import com.example.priscillaclient.api.ApiTaskLegacy;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.Profile;

import org.json.JSONObject;

public class GetProfileData extends ApiTaskLegacy {

    public GetProfileData(HttpResponse context) {
        super(context);
    }

    @Override
    protected Profile doInBackground(String... strings) {

        // Return from cache
        if (client.profile != null)
            return client.profile;

        try {
            HttpConnection connection = new HttpConnection("/get-profile-data", "GET", false);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorMessage());
                return client.profile;
            }

            JSONObject json = new JSONObject(connection.getResponse());

            client.profile = new Profile(json);

            connection.disconnect();

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return client.profile;
    }
}
