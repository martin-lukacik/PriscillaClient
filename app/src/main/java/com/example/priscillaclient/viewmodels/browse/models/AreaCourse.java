package com.example.priscillaclient.viewmodels.browse.models;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class AreaCourse {

    public final int id;
    public final String title;
    public final int course_order;
    public final String description;
    public final String course_status; // opt

    public final int task_finished;
    public final int program_finished;

    public final int content_all;
    public final int task_all;
    public final int program_all;

    public final String start_date; // opt
    public final String finish_date; // opt

    public enum CourseStatus {
        NONE,
        OPENED,
    }

    public CourseStatus status = CourseStatus.NONE;

    public AreaCourse(JSONObject json) throws JSONException {
        id = json.getInt("id");
        title = json.getString("title");
        course_order = json.getInt("course_order");
        description = json.getString("description");
        task_finished = json.getInt("task_finished");
        program_finished = json.getInt("program_finished");
        content_all = json.getInt("content_all");
        task_all = json.getInt("task_all");
        program_all = json.getInt("program_all");

        course_status = json.optString("course_status");
        start_date = json.optString("start_date");
        finish_date = json.optString("finish_date");

        if (course_status.equalsIgnoreCase("opened")) {
            status = CourseStatus.OPENED;
        }
    }

    @NotNull
    @Override
    public String toString() {
        return title;
    }

    //https://app.priscilla.fitped.eu/area-all-courses/1
}
