package com.example.priscillaclient.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaskEval {

    class Answer {
        public String answer;
        public String feedback;
        public int rating;

        public Answer(JSONObject json) throws JSONException {
            answer = json.getString("answer");
            feedback = json.optString("feedback");
            rating = json.getInt("rating");
        }
    }

    public int rating;

    public ArrayList<Answer> answers = new ArrayList<>();

    public TaskEval(JSONObject json) throws JSONException {
        rating = json.getInt("rating");

        JSONArray jAnswers = json.getJSONArray("answers");
        for (int i = 0; i < jAnswers.length(); ++i) {
            answers.add(new Answer(jAnswers.getJSONObject(i)));
        }
    }
}
