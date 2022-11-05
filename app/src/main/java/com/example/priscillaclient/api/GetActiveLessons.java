package com.example.priscillaclient.api;

import android.content.Context;
import android.os.AsyncTask;

import com.example.priscillaclient.Client;
import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.TaskActivity;
import com.example.priscillaclient.models.Lesson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;


// TODO Get lessons & tasks at the same time, need them in one view anyway
public class GetActiveLessons extends AsyncTask<String, String, ArrayList<Lesson>> {

    final Context context;
    final int activeCourseId;
    public GetActiveLessons(Context context, int activeCourseId) {
        super();
        this.context = context;
        this.activeCourseId = activeCourseId;
    }

    @Override
    protected ArrayList<Lesson> doInBackground(String... strings) {
        try {
            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/get-active-lessons2/" + activeCourseId, "GET", false);

            int status = connection.getResponseCode();
            String message = connection.getResponseMessage();

            InputStream responseStream = connection.getInputStream();

            String responseStr = "";
            try (Scanner scanner = new Scanner(responseStream)) {
                responseStr = scanner.useDelimiter("\\A").next();
            }

            JSONArray json = new JSONObject(responseStr).getJSONArray("lesson_list");

            Client client = Client.getInstance();

            client.lessons = new ArrayList<>();

            for (int i = 0; i < json.length(); ++i) {
                Lesson c = new Lesson(json.getJSONObject(i));
                client.lessons.add(c);
            }

            return client.lessons;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(ArrayList<Lesson> tasks) {
        ((TaskActivity) context).onUpdate(tasks);
    }
}
