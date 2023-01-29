package com.example.priscillaclient.api.tasks;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.viewmodels.app.models.Help;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class GetHelp implements Callable<Help> {

    private final int taskId;

    public GetHelp(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public Help call() throws Exception {
        HttpConnection connection = new HttpConnection("/get-my-help/" + taskId, "GET");

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONObject json = new JSONObject(connection.getResponse());

        return new Help(json);
    }
}
