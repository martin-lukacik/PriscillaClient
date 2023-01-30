package com.example.priscillaclient.viewmodels.app.models;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.rosemoe.sora.util.ArrayList;

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
            answer = json.optString("answers");
            type = AnswerType.ANSWER;
        }

        feedback = json.optString("feedback");
        rating = json.optInt("rating");
    }

    public ArrayList<String> getAnswerList() {
        ArrayList<String> answers = new ArrayList<>();

        try {
            JSONArray json = new JSONArray(answer);
            for (int i = 0; i < json.length(); ++i) {
                answers.add(json.getJSONObject(i).getString("answer"));
            }
        } catch (Exception ignore) { }

        return answers;
    }
}
