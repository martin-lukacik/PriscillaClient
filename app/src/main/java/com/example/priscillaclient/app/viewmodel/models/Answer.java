package com.example.priscillaclient.app.viewmodel.models;

import org.json.JSONObject;

public class Answer {
    public final String answer;
    public final String feedback;
    public final int rating;

    public Answer(JSONObject json) {
        if (json == null) {
            json = new JSONObject();
        }
        answer = json.optString("answer");
        feedback = json.optString("feedback");
        rating = json.optInt("rating");
    }
}
