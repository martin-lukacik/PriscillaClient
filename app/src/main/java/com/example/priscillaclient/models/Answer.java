package com.example.priscillaclient.models;

import org.json.JSONObject;

public class Answer {
    public String answer;
    public String feedback;
    public int rating;

    public Answer(JSONObject json) {
        if (json == null)
            return;
        answer = json.optString("answer");
        feedback = json.optString("feedback");
        rating = json.optInt("rating");
    }
}
