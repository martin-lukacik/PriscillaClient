package com.example.priscillaclient.api.user;

import com.example.priscillaclient.api.ApiTask;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.RegistrationData;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetRegistrationData extends ApiTask {

    public GetRegistrationData(HttpResponse context) {
        super(context);
    }

    @Override
    protected RegistrationData doInBackground(String... strings) {

        // Return from cache
        if (client.registrationData != null && !client.registrationData.isEmpty()) {
            return client.registrationData;
        }

        try {
            HttpConnection connection = new HttpConnection("/get-registration-data", "GET", false);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorStream());
                return client.registrationData;
            }

            JSONObject json = new JSONObject(connection.getResponse());

            JSONArray languages = json.getJSONArray("languages");
            JSONArray countries = json.getJSONArray("countries");
            JSONArray groups = json.getJSONArray("groups");
            JSONArray themes = json.getJSONArray("themes");

            client.registrationData = new RegistrationData(languages, countries, groups, themes);

            connection.disconnect();

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return client.registrationData;
    }
}
