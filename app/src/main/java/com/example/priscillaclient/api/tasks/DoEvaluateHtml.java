package com.example.priscillaclient.api.tasks;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.viewmodels.app.models.Task;
import com.example.priscillaclient.viewmodels.app.models.TaskResult;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class DoEvaluateHtml implements Callable<TaskResult> {
    private final Task task;
    private final String answersJson;
    private final int timeLength;
    private final String desc;

    public DoEvaluateHtml(Task task, String answersJson, int timeLength, String desc) {
        this.answersJson = answersJson;
        this.task = task;
        this.timeLength = timeLength;
        this.desc = desc;
    }

    @Override
    public TaskResult call() throws Exception {
        HttpConnection connection = new HttpConnection("/task-evaluate", "POST");

        JSONObject json = new JSONObject();
        json.put("answer_list",
                "[\"" + answersJson
                .replaceAll("\n", "\\\\n")
                .replaceAll("\t", "\\\\t")
                .replaceAll("\"", "\\\\\"")
                + "\"]"); // TODO BUG </html> -> <\/html>
        json.put("description", "[{\"res\":false,\"desc\":\"" + desc + "\"}]");
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