package com.example.priscillaclient.user.api;

import com.example.priscillaclient.util.HttpConnection;
import com.example.priscillaclient.user.viewmodel.models.Profile;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class GetProfile implements Callable<Profile> {
    @Override
    public Profile call() throws Exception {
        HttpConnection connection = new HttpConnection("/get-profile-data", "GET");

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONObject json = new JSONObject(connection.getResponse());

        return new Profile(json);
    }
}
