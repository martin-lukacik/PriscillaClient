package com.example.priscillaclient.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Task {
    public int task_id;
    public int task_type_id;
    public int score;
    public int max_score;
    public int first_time;
    public int passed;
    public int help_showed;
    public int answer_showed;
    public int task_order;
    public int discuss_count;
    public String globals;
    public String content;
    public String start_time;
    public String end_time;
    public String answer;
    public String comment;
    public int clarity;
    public int difficulty;

    String help;
    public ArrayList<String> answers = null;
    public ArrayList<String> fakes = null;

    public TaskType type;
    static TaskType[] taskTypes = TaskType.values();

    public Task(JSONObject json) throws JSONException {

        task_id = json.getInt("task_id");
        task_type_id = json.getInt("task_type_id");
        type = taskTypes[json.getInt("task_type_id")];
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
            JSONObject j = new JSONObject(content);
            content = j.getString("content");

            if (j.has("help")) {
                help = json.optString("help");
            }

            if (j.has("answer_list")) {
                answers = new ArrayList<>();
                JSONArray jAnswers = j.getJSONArray("answer_list");
                for (int i = 0; i < jAnswers.length(); ++i) {
                    answers.add(jAnswers.getJSONObject(i).getString("answer"));
                }
            }

            if (j.has("fakes")) {
                fakes = new ArrayList<>();
                JSONArray jFakes = j.getJSONArray("fakes");
                for (int i = 0; i < jFakes.length(); ++i) {
                    fakes.add(jFakes.optString(i));
                }
            }
        } catch (Exception ignore) { }
    }
}
