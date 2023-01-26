package com.example.priscillaclient.api.app;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.viewmodel.app.models.Chapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class GetChapters implements Callable<ArrayList<Chapter>> {

    private final int courseId;

    public GetChapters(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public ArrayList<Chapter> call() throws Exception {
        HttpConnection connection = new HttpConnection("/get-active-chapters2/" + courseId, "GET", false);

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        InputStream responseStream = connection.getInputStream();
        String response = new Scanner(responseStream).useDelimiter("\\A").next();
        JSONArray json = new JSONObject(response).getJSONArray("chapter_list");

        ArrayList<Chapter> chapters = new ArrayList<>();
        for (int i = 0; i < json.length(); ++i) {
            Chapter c = new Chapter(json.getJSONObject(i));
            chapters.add(c);
        }

        return chapters;
    }
}
