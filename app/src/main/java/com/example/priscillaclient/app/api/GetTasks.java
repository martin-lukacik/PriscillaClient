package com.example.priscillaclient.app.api;

import com.example.priscillaclient.util.HttpConnection;
import com.example.priscillaclient.app.viewmodel.models.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GetTasks implements Callable<ArrayList<Task>> {

    private final int courseId;
    private final int chapterId;
    private final int lessonId;

    public GetTasks(int courseId, int chapterId, int lessonId) {
        this.courseId = courseId;
        this.chapterId = chapterId;
        this.lessonId = lessonId;
    }

    @Override
    public ArrayList<Task> call() throws Exception {
        HttpConnection connection = new HttpConnection("/get-active-tasks2/" + courseId + "/" + chapterId + "/" + lessonId, "GET");

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONObject j = new JSONObject(connection.getResponse());
        Task.user_course_id = j.getInt("user_course_id");
        JSONArray json = j.getJSONArray("task_list");

        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < json.length(); ++i) {
            Task t = new Task(json.getJSONObject(i));
            tasks.add(t);
        }

        return tasks;
    }
}
