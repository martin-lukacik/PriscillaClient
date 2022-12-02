package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {
    int task_id;
    int task_type_id;
    int score;
    int max_score;
    int first_time;
    int passed;
    int help_showed;
    int answer_showed;
    int task_order;
    int discuss_count;
    String globals;
    public String content;
    String start_time;
    String end_time;
    String answer;
    String comment;
    int clarity;
    int difficulty;

    public Task(JSONObject json) throws JSONException {
        task_id = json.getInt("task_id");
        task_type_id = json.getInt("task_type_id");
        score = json.getInt("score");
        max_score = json.getInt("max_score");
        first_time = json.getInt("first_time");
        passed = json.getInt("passed");
        help_showed = json.getInt("help_showed");
        answer_showed = json.getInt("answer_showed");
        task_order = json.getInt("task_order");
        discuss_count = json.getInt("discuss_count");
        globals = json.getString("globals");
        start_time = json.getString("start_time");
        end_time = json.getString("end_time");
        answer = json.getString("answer");
        comment = json.getString("comment");
        clarity = json.getInt("clarity");
        difficulty = json.getInt("difficulty");

        content = json.getString("content");

        try {
            content = json.getJSONObject("content").getString("content");
        } catch (Exception ignored) {

        }
    }
}
