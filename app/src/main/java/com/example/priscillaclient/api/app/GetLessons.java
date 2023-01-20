package com.example.priscillaclient.api.app;

import com.example.priscillaclient.api.ApiTask;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.views.fragments.FragmentBase;
import com.example.priscillaclient.models.Lesson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorStream());
                return client.lessons;
            }

            JSONArray json = new JSONObject(connection.getResponse()).getJSONArray("lesson_list");

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
