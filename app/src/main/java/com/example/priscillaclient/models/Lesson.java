package com.example.priscillaclient.models;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class Lesson {
    public final int order;
    public final int id;

    public final String name;

    public final int tasks_finished;
    public final int tasks_nonfinished;

    public final int programs_finished;
    public final int programs_nonfinished;

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
