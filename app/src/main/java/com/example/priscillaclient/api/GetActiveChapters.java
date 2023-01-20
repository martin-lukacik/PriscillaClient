package com.example.priscillaclient.api;

import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.models.Chapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class GetActiveChapters extends ApiTask {

    final int courseId;

    public GetActiveChapters(FragmentBase fragment, int courseId) {
        super(fragment);
        this.courseId = courseId;
    }

    @Override
    protected ArrayList<Chapter> doInBackground(String... strings) {

        // Use the cached result
        if (client.lastCourseId == courseId) {
            if (!client.chapters.isEmpty())
                return client.chapters;
        }

        client.lastCourseId = courseId;

        try {
            HttpURLConnection connection = getConnection("/get-active-chapters2/" + courseId, "GET", false);

            int status = connection.getResponseCode();
            if (status >= 400 && status < 600) {
                logError(connection.getErrorStream());
                return client.chapters;
            }

            InputStream responseStream = connection.getInputStream();
            String response = new Scanner(responseStream).useDelimiter("\\A").next();
            JSONArray json = new JSONObject(response).getJSONArray("chapter_list");

            client.chapters.clear();
            for (int i = 0; i < json.length(); ++i) {
                Chapter c = new Chapter(json.getJSONObject(i));
                client.chapters.add(c);
            }

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return client.chapters;
    }
}
