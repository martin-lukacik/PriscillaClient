package com.example.priscillaclient.api.app;

import com.example.priscillaclient.api.ApiTaskLegacy;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.Course;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetCourses extends ApiTaskLegacy {

    public GetCourses(HttpResponse context) {
        super(context);

        //showProgressDialog();
    }

    @Override
    protected ArrayList<Course> doInBackground(String... strings) {

        // Return from cache
        if (!client.courses.isEmpty())
            return client.courses;

        try {
            HttpConnection connection = new HttpConnection("/get-active-user-courses2", "GET", false);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorMessage());
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
