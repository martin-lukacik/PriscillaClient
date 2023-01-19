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

        if (client.courses != null)
            return client.courses;

        try {
            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/get-active-user-courses2", "GET", false);

            int status = connection.getResponseCode();

            if (status >= 400 && status < 600) {
                logError(connection.getErrorStream());
                return null;
            }

            InputStream responseStream = connection.getInputStream();

            String responseStr = "";
            try (Scanner scanner = new Scanner(responseStream)) {
                responseStr = scanner.useDelimiter("\\A").next();
            }

            client.courses = new ArrayList<>();

            JSONArray json = new JSONObject(responseStr).getJSONArray("list");

            for (int i = 0; i < json.length(); ++i) {
                JSONObject j = json.getJSONObject(i);
                client.courses.add(new Course(j));
            }

            return client.courses;

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return null;
    }
}
