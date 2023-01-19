package com.example.priscillaclient.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.MainActivity;
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
        try {
            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/get-active-user-courses2", "GET", false);

            int status = connection.getResponseCode();
            String message = connection.getResponseMessage();

            InputStream responseStream = connection.getInputStream();

            String responseStr = "";
            try (Scanner scanner = new Scanner(responseStream)) {
                responseStr = scanner.useDelimiter("\\A").next();
            }

            Log.i("COURSES", responseStr);

            Client client = Client.getInstance();

            client.courses = new ArrayList<>();

            JSONArray json = new JSONObject(responseStr).getJSONArray("list");
            //JSONArray json = new JSONArray(temp);

            for (int i = 0; i < json.length(); ++i) {
                JSONObject j = json.getJSONObject(i);
                client.courses.add(new Course(j));
                client.courses.get(i).fillUserData(j);
            }

            return client.courses;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
