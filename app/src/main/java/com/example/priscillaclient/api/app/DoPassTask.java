package com.example.priscillaclient.api.app;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.viewmodel.app.models.Task;
import com.example.priscillaclient.viewmodel.app.models.TaskResult;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class DoPassTask implements Callable<TaskResult> {

    Task task;
    public DoPassTask(Task task) {
        this.task = task;
    }

    @Override
    public TaskResult call() throws Exception {
        HttpConnection connection = new HttpConnection("/set-passed-tasks-content2", "POST");

        JSONObject json = new JSONObject();
        json.put("task_id", task.task_id);
        json.put("user_course_id", task.user_course_id);

        connection.sendRequest(json);

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        return new TaskResult(100);
    }
}
