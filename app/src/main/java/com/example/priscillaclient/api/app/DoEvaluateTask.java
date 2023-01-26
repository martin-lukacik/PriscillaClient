package com.example.priscillaclient.api.app;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.models.TaskResult;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class DoEvaluateTask implements Callable<TaskResult> {

    String answersJson;
    int taskId;
    int taskTypeId;
    int timeLength;

    public DoEvaluateTask(String answersJson, int taskId, int taskTypeId, int timeLength) {
        this.answersJson = answersJson;
        this.taskId = taskId;
        this.taskTypeId = taskTypeId;
        this.timeLength = timeLength;
    }

    @Override
    public TaskResult call() throws Exception {
        HttpConnection connection = new HttpConnection("/task-evaluate2", "POST", true);

        JSONObject json = new JSONObject();
        json.put("answer_list", answersJson);
        json.put("activity_type", "chapter"); // TODO hardcoded
        json.put("task_id", taskId);
        json.put("task_type_id", taskTypeId);
        json.put("time_length", timeLength);

        connection.sendRequest(json);

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONObject response = new JSONObject(connection.getResponse());
        return new TaskResult(response.getJSONObject("result"));
    }
}
