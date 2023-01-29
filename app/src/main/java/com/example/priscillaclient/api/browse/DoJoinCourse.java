package com.example.priscillaclient.api.browse;

import com.example.priscillaclient.api.HttpConnection;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class DoJoinCourse implements Callable<String> {

    private final int courseId;
    public DoJoinCourse(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String call() throws Exception {
        HttpConnection connection = new HttpConnection("/write-user-course/" + courseId, "POST");

        JSONObject json = new JSONObject();

        connection.sendRequest(json);

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        return "OK";
    }
}
