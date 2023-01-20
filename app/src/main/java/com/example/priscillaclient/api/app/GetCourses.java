package com.example.priscillaclient.api.app;

import com.example.priscillaclient.api.ApiTask;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.views.fragments.FragmentBase;
import com.example.priscillaclient.models.Course;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetCourses extends ApiTask {

    public GetCourses(FragmentBase fragment) {
        super(fragment);
    }

    @Override
    protected ArrayList<Course> doInBackground(String... strings) {

        // Use the cached result
        if (!client.courses.isEmpty())
            return client.courses;

        try {
            HttpConnection connection = new HttpConnection("/get-active-user-courses2", "GET", false);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorStream());
                return client.courses;
            }

            JSONArray json = new JSONObject(connection.getResponse()).getJSONArray("list");

            client.courses.clear();
            for (int i = 0; i < json.length(); ++i) {
                JSONObject j = json.getJSONObject(i);
                client.courses.add(new Course(j));
            }

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return client.courses;
    }
}
