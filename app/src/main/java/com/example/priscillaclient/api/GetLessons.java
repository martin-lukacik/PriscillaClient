package com.example.priscillaclient.api;

import com.example.priscillaclient.api.ApiTask;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.models.Lesson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class GetLessons extends ApiTask {

    final int chapterId;
    public GetLessons(FragmentBase fragment, int chapterId) {
        super(fragment);
        this.chapterId = chapterId;
        client.lastChapterId = chapterId;
    }

    @Override
    protected ArrayList<Lesson> doInBackground(String... strings) {
        try {
            HttpConnection connection = new HttpConnection("/get-active-lessons2/" + chapterId, "GET", false);

            int status = connection.getResponseCode();
            if (status >= 400 && status < 600) {
                logError(connection.getErrorStream());
                return client.lessons;
            }

            InputStream responseStream = connection.getInputStream();
            String response = new Scanner(responseStream).useDelimiter("\\A").next();
            JSONArray json = new JSONObject(response).getJSONArray("lesson_list");

            client.lessons.clear();
            for (int i = 0; i < json.length(); ++i) {
                Lesson c = new Lesson(json.getJSONObject(i));
                client.lessons.add(c);
            }

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return client.lessons;
    }
}
