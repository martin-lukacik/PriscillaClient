package com.example.priscillaclient.models;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class Lesson {
    int order;
    public int id;

    public String name;

    public int tasks_finished;
    public int tasks_nonfinished;

    public int programs_finished;
    public int programs_nonfinished;

    public Lesson(JSONObject json) throws JSONException {
        order = json.getInt("lesson_order");
        id = json.getInt("lesson_id");
        name = json.getString("lesson_name");
        tasks_finished = json.getInt("tasks_finished");
        tasks_nonfinished = json.getInt("tasks_nonfinished");
        programs_finished = json.getInt("programs_finished");
        programs_nonfinished = json.getInt("programs_nonfinished");
    }

    @NotNull
    @Override
    public String toString() {
        return name;
    }
}
