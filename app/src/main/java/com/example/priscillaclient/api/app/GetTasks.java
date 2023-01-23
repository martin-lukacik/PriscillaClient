package com.example.priscillaclient.api.app;

import com.example.priscillaclient.api.ApiTask;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.views.fragments.FragmentBase;
import com.example.priscillaclient.models.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetTasks extends ApiTask {

    int courseId;
    int chapterId;
    int lessonId;

    public GetTasks(FragmentBase fragment, int lessonId) {
        super(fragment);
        this.courseId = client.lastCourseId;
        this.chapterId = client.lastChapterId;
        this.lessonId = lessonId;
        client.lastLessonId = lessonId;
    }

    @Override
    protected ArrayList<Task> doInBackground(String... strings) {
        try {
            HttpConnection connection = new HttpConnection("/get-active-tasks2/" + courseId + "/" + chapterId + "/" + lessonId, "GET", false);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorMessage());
                return client.tasks;
            }

            JSONObject j = new JSONObject(connection.getResponse());
            client.lastUserCourseId = j.getInt("user_course_id");
            JSONArray json = j.getJSONArray("task_list");

            client.tasks.clear();
            for (int i = 0; i < json.length(); ++i) {
                Task t = new Task(json.getJSONObject(i));
                client.tasks.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return client.tasks;
    }
}
