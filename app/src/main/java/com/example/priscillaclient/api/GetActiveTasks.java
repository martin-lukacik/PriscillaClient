package com.example.priscillaclient.api;

import android.content.Context;
import android.os.AsyncTask;

import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.TaskActivity;
import com.example.priscillaclient.models.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class GetActiveTasks extends AsyncTask<String, String, ArrayList<Task>> {

    final Context context;

    int courseId;
    int chapterId;
    int lessonId;

    public GetActiveTasks(Context context, int courseId, int chapterId, int lessonId) {
        super();
        this.context = context;
        this.courseId = courseId;
        this.chapterId = chapterId;
        this.lessonId = lessonId;
    }

    @Override
    protected ArrayList<Task> doInBackground(String... strings) {

        try {

            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/get-active-tasks2/" + courseId + "/" + chapterId + "/" + lessonId, "GET", false);

            int status = connection.getResponseCode();
            String message = connection.getResponseMessage();

            InputStream responseStream = connection.getInputStream();

            String responseStr = "";
            try (Scanner scanner = new Scanner(responseStream)) {
                responseStr = scanner.useDelimiter("\\A").next();
            }


            JSONArray json = new JSONObject(responseStr).getJSONArray("task_list");

            Client client = Client.getInstance();

            client.tasks = new ArrayList<>();

            for (int i = 0; i < json.length(); ++i) {
                Task t = new Task(json.getJSONObject(i));
                client.tasks.add(t);
            }

            return client.tasks;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Task> tasks) {
        ((TaskActivity) context).onUpdate(tasks);
    }

    // https://app.priscilla.fitped.eu/get-active-tasks2/1/55/104
}
