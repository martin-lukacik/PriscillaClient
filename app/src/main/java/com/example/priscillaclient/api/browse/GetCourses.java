package com.example.priscillaclient.api.browse;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.viewmodels.app.models.Course;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GetCourses implements Callable<ArrayList<Course>> {

    public GetCourses() { }

    @Override
    public ArrayList<Course> call() throws Exception {
        HttpConnection connection = new HttpConnection("/get-active-user-courses2", "GET");

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        ArrayList<Course> courses = new ArrayList<>();
        JSONArray json = new JSONObject(connection.getResponse()).getJSONArray("list");
        for (int i = 0; i < json.length(); ++i) {
            JSONObject j = json.getJSONObject(i);
            courses.add(new Course(j));
        }

        return courses;
    }
}
