package com.example.priscillaclient.api;

import android.content.Context;
import android.os.AsyncTask;

import com.example.priscillaclient.AreaCourseActivity;
import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.models.AreaCourse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class GetAreaCourses extends AsyncTask<String, String, ArrayList<AreaCourse>> {

    final Context context;
    final int areaId;

    public GetAreaCourses(Context context, int areaId) {
        super();
        this.context = context;
        this.areaId = areaId;
    }

    @Override
    protected ArrayList<AreaCourse> doInBackground(String... strings) {

        try {
            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/area-all-courses/" + areaId, "GET", false);

            int status = connection.getResponseCode();
            String message = connection.getResponseMessage();

            InputStream responseStream = connection.getInputStream();

            String responseStr;
            try (Scanner scanner = new Scanner(responseStream)) {
                responseStr = scanner.useDelimiter("\\A").next();
            }


            JSONArray json = new JSONObject(responseStr).getJSONArray("list");

            Client client = Client.getInstance();

            client.areaCourses = new ArrayList<>();

            for (int i = 0; i < json.length(); ++i) {
                AreaCourse a = new AreaCourse(json.getJSONObject(i));
                client.areaCourses.add(a);
            }

            return client.areaCourses;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<AreaCourse> areaCourses) {
        ((AreaCourseActivity) context).onUpdate(areaCourses);
    }
}

