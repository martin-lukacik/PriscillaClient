package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class AreaCourse {

    public int id;
    String title;
    int course_order;
    String description;
    String course_status; // opt

    int task_finished;
    int program_finished;

    int content_all;
    int task_all;
    int program_all;

    String start_date; // opt
    String finish_date; // opt

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

    @Override
    public String toString() {
        return title;
    }

    //https://app.priscilla.fitped.eu/area-all-courses/1
}
