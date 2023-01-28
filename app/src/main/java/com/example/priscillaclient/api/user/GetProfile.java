package com.example.priscillaclient.api.user;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.viewmodel.user.models.Profile;

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
