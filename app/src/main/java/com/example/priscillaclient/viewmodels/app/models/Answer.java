package com.example.priscillaclient.viewmodels.app.models;

import org.json.JSONObject;

public class Answer {
    public final String answer;
    public final String feedback;
    public final int rating;

    public final AnswerType type;

    public enum AnswerType {
        HELP,
        ANSWER,
    }

    public Answer(JSONObject json) {
        if (json == null) {
            json = new JSONObject();
        }

        if (json.has("help")) {
            answer = json.optString("help");
            type = AnswerType.HELP;
        } else {
            answer = json.optString("answer");
            type = AnswerType.ANSWER;
        }

        feedback = json.optString("feedback");
        rating = json.optInt("rating");
    }
}
