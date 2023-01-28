package com.example.priscillaclient.api.tasks;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.viewmodels.app.models.Task;
import com.example.priscillaclient.viewmodels.app.models.TaskResult;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class DoEvaluateTask implements Callable<TaskResult> {

    Task task;
    String answersJson;
    int timeLength;

    public DoEvaluateTask(Task task, String answersJson, int timeLength) {
        this.answersJson = answersJson;
        this.task = task;
        this.timeLength = timeLength;
    }

    @Override
    public TaskResult call() throws Exception {
        HttpConnection connection = new HttpConnection("/task-evaluate2", "POST");

        JSONObject json = new JSONObject();
        json.put("answer_list", answersJson);
        json.put("activity_type", "chapter"); // TODO hardcoded value "chapter"
        json.put("task_id", task.task_id);
        json.put("task_type_id", task.task_type_id);
        json.put("time_length", timeLength);

        connection.sendRequest(json);

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONObject response = new JSONObject(connection.getResponse());
        return new TaskResult(response.getJSONObject("result"));
    }
}
