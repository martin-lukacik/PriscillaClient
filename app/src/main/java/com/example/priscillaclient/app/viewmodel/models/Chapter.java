package com.example.priscillaclient.app.viewmodel.models;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class Chapter {

    public final int order;
    public final int id;
    public final String name;
    public final String icon;

    public final int tasks_finished;
    public final int tasks_nonfinished;

    public final int programs_finished;
    public final int programs_nonfinished;

    public Chapter(JSONObject json) throws JSONException {
        order = json.getInt("chapter_order");
        id = json.getInt("chapter_id");
        name = json.getString("chapter_name");
        icon = json.getString("chapter_icon");
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
