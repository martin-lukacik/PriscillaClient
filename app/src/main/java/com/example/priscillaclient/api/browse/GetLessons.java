package com.example.priscillaclient.api.browse;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.viewmodel.app.models.Lesson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GetLessons implements Callable<ArrayList<Lesson>> {

    private final int chapterId;

    public GetLessons(int chapterId) {
        this.chapterId = chapterId;
    }

    @Override
    public ArrayList<Lesson> call() throws Exception {
        HttpConnection connection = new HttpConnection("/get-active-lessons2/" + chapterId, "GET");

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONArray json = new JSONObject(connection.getResponse()).getJSONArray("lesson_list");

        ArrayList<Lesson> lessons = new ArrayList<>();
        for (int i = 0; i < json.length(); ++i) {
            Lesson c = new Lesson(json.getJSONObject(i));
            lessons.add(c);
        }

        return lessons;
    }
}
