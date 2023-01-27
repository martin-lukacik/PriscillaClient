package com.example.priscillaclient.app.viewmodel.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaskResult {

    public int rating;

    public final ArrayList<Answer> answers = new ArrayList<>();

    public String compilation = null;
    public String execution = null;

    public TaskResult(int rating) {
        this.rating = rating;
    }

    public TaskResult(JSONObject json) throws JSONException {
        this.rating = json.optInt("rating", -1);

        if (this.rating == -1) {
            this.rating = json.getJSONObject("result").getInt("rating");
        }

        JSONArray jAnswers = json.optJSONArray("answers");
        if (jAnswers != null) {
            for (int i = 0; i < jAnswers.length(); ++i) {
                answers.add(new Answer(jAnswers.optJSONObject(i)));
            }
        } else {
            compilation = json.getString("compilation");
            execution = json.getString("execution");
        }
    }
}
