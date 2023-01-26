package com.example.priscillaclient.user.api;

import com.example.priscillaclient.util.HttpConnection;
import com.example.priscillaclient.user.viewmodel.models.Settings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.Callable;

public class GetSettings implements Callable<Settings> {
    @Override
    public Settings call() throws Exception {
        HttpConnection connection = new HttpConnection("/get-registration-data", "GET");

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONObject json = new JSONObject(connection.getResponse());

        JSONArray languages = json.getJSONArray("languages");
        JSONArray countries = json.getJSONArray("countries");
        JSONArray groups = json.getJSONArray("groups");
        JSONArray themes = json.getJSONArray("themes");

        return new Settings(languages, countries, groups, themes);
    }
}
