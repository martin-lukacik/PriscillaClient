package com.example.priscillaclient.api.tasks;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.viewmodels.app.models.Answer;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class GetAnswer implements Callable<Answer> {

    private final int taskId;
    private final Answer.AnswerType type;

    public GetAnswer(int taskId, Answer.AnswerType type) {
        this.taskId = taskId;
        this.type = type;
    }

    @Override
    public Answer call() throws Exception {
        String endPoint = "/get-my-help/";
        if (type == Answer.AnswerType.ANSWER)
            endPoint = "/get-my-answer/";

        HttpConnection connection = new HttpConnection(endPoint + taskId, "GET");

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONObject json = new JSONObject(connection.getResponse());

        return new Answer(json);
    }
}
