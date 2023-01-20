package com.example.priscillaclient.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaskEval {

    public int rating;

    public ArrayList<Answer> answers = new ArrayList<>();

    public TaskEval(int rating) {
        this.rating = rating;
    }

    public TaskEval(JSONObject json) throws JSONException {
        rating = json.getInt("rating");

        JSONArray jAnswers = json.getJSONArray("answers");
        for (int i = 0; i < jAnswers.length(); ++i) {
            answers.add(new Answer(jAnswers.optJSONObject(i)));
        }
    }
}
