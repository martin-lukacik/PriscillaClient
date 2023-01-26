package com.example.priscillaclient.api.user;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.viewmodel.user.models.User;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class GetUser implements Callable<User> {
    @Override
    public User call() throws Exception {
        HttpConnection connection = new HttpConnection("/get-full-user-parameters", "GET", false);

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONObject response = new JSONObject(connection.getResponse());

        return new User(response);
    }
}
