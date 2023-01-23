package com.example.priscillaclient.models;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class Course {

    public boolean isPinned = false;

    public final int course_id;
    public final String name;
    public final String description;
    public final String area_color;

    public final int passed;
    public final int all;
    public final int content_count;
    public final int content_passed;
    public final int task_count;
    public final int task_passed;
    public final int program_count;
    public final int program_passed;
    public final int score;
    public final int max_score;
    public final int score_task;
    public final int score_task_max;
    public final int score_program;
    public final int score_program_max;
    public final int progress;

    public Course(JSONObject json) throws JSONException {
        course_id = json.getInt("course_id");
        name = json.getString("name");
        description = json.getString("description");
        area_color = json.getString("area_color");

        passed = json.optInt("passed");
        all = json.optInt("all");
        content_count = json.optInt("content_count");
        content_passed = json.optInt("content_passed");
        task_count = json.optInt("task_count");
        task_passed = json.optInt("task_passed");
        program_count = json.optInt("program_count");
        program_passed = json.optInt("program_passed");
        score = json.optInt("score");
        max_score = json.optInt("max_score");
        score_task = json.optInt("score_task");
        score_task_max = json.optInt("score_task_max");
        score_program = json.optInt("score_program");
        score_program_max = json.optInt("score_program_max");
        progress = json.optInt("progress");
    }

    @NotNull
    @Override
    public String toString() {
        return name;
    }
}
