package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Course {

    // Required

    public int course_id;
    public String name;
    public String description;
    public String area_color;

    public Course(JSONObject json) throws JSONException {
        course_id = json.getInt("course_id");
        name = json.getString("name");
        description = json.getString("description");
        area_color = json.getString("area_color");
    }

    @Override
    public String toString() {
        return name;
    }

    // Optional

    HashMap<String, Integer> userData;

    public Integer getUserData(String key) {
        return userData.get(key);
    }

    public void fillUserData(JSONObject json) throws JSONException {

        userData = new HashMap<>();
        fill(json, "passed");
        fill(json, "all");

        fill(json, "content_count");
        fill(json, "content_passed");

        fill(json, "task_count");
        fill(json, "task_passed");

        fill(json, "program_count");
        fill(json, "program_passed");

        fill(json, "score");
        fill(json, "max_score");

        fill(json, "score_task");
        fill(json, "max_score_task");

        fill(json, "score_program");
        fill(json, "max_score_program");

        fill(json, "progress");
    }

    private void fill(JSONObject json, String map) throws JSONException {
        userData.put(map, json.getInt(map));
    }
/*
    public int passed;
    public int all;

    public int content_count;
    public int content_passed;

    public int task_count;
    public int task_passed;

    public int program_count;
    public int program_passed;

    public int score;
    public int max_score;

    public int score_task;
    public int score_task_max;

    public int score_program;
    public int score_program_max;

    public int progress; // percentage

 */
}
