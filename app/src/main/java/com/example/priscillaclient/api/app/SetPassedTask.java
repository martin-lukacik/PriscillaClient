package com.example.priscillaclient.api.app;

import com.example.priscillaclient.api.ApiTaskLegacy;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.api.HttpResponse;

import org.json.JSONObject;

public class SetPassedTask extends ApiTaskLegacy {

    int taskId;

    public SetPassedTask(HttpResponse context, int taskId) {
        super(context);

        this.taskId = taskId;
    }

    @Override
    protected String doInBackground(String... strings) {

        String message = "";

        try {
            HttpConnection connection = new HttpConnection("/set-passed-tasks-content2", "POST", true);

            JSONObject json = new JSONObject();
            json.put("task_id", taskId);
            json.put("user_course_id", client.lastUserCourseId);

            connection.sendRequest(json);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorMessage());
            }

            return connection.getResponse();
        } catch (Exception e) {
            logError(e.getMessage());
        }

        return message;
    }
}
