package com.example.priscillaclient.api;

import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.models.Chapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class GetActiveChapters extends ApiTask {

    final int activeCourseId;

    public GetActiveChapters(FragmentBase fragment, int activeCourseId) {
        super(fragment);
        this.activeCourseId = activeCourseId;
    }

    @Override
    protected ArrayList<Chapter> doInBackground(String... strings) {
        try {
            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/get-active-chapters2/" + activeCourseId, "GET", false);

            int status = connection.getResponseCode();
            String message = connection.getResponseMessage();

            InputStream responseStream = connection.getInputStream();

            String responseStr;
            try (Scanner scanner = new Scanner(responseStream)) {
                responseStr = scanner.useDelimiter("\\A").next();
            }

            JSONArray json = new JSONObject(responseStr).getJSONArray("chapter_list");

            Client client = Client.getInstance();

            client.chapters = new ArrayList<>();

            for (int i = 0; i < json.length(); ++i) {
                Chapter c = new Chapter(json.getJSONObject(i));
                client.chapters.add(c);
            }

            return client.chapters;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
