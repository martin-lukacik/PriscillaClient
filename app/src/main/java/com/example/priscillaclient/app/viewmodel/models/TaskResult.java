package com.example.priscillaclient.app.viewmodel.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaskResult {

    public final int rating;

    public final ArrayList<Answer> answers = new ArrayList<>();

    public TaskResult(int rating) {
        this.rating = rating;
    }

    public TaskResult(JSONObject json) throws JSONException {
        rating = json.getInt("rating");

        JSONArray jAnswers = json.getJSONArray("answers");
        for (int i = 0; i < jAnswers.length(); ++i) {
            answers.add(new Answer(jAnswers.optJSONObject(i)));
        }
    }
}
