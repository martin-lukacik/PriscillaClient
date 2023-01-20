package com.example.priscillaclient.api;

import com.example.priscillaclient.api.ApiTask;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.models.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

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
            HttpURLConnection connection = getConnection("/get-active-tasks2/" + courseId + "/" + chapterId + "/" + lessonId, "GET", false);

            int status = connection.getResponseCode();
            if (status >= 400 && status < 600) {
                logError(connection.getErrorStream());
                return client.tasks;
            }

            InputStream responseStream = connection.getInputStream();
            String response = new Scanner(responseStream).useDelimiter("\\A").next();
            JSONArray json = new JSONObject(response).getJSONArray("task_list");

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
