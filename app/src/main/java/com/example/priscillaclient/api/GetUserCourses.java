package com.example.priscillaclient.api;

import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.models.Course;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class GetUserCourses extends ApiTask {

    public GetUserCourses(FragmentBase fragment) {
        super(fragment);
    }

    @Override
    protected ArrayList<Course> doInBackground(String... strings) {

        // Use the cached result
        if (!client.courses.isEmpty())
            return client.courses;

        try {
            HttpURLConnection connection = getConnection("/get-active-user-courses2", "GET", false);

            int status = connection.getResponseCode();
            if (status >= 400 && status < 600) {
                logError(connection.getErrorStream());
                return null;
            }

            InputStream responseStream = connection.getInputStream();
            String response = new Scanner(responseStream).useDelimiter("\\A").next();
            JSONArray json = new JSONObject(response).getJSONArray("list");

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
