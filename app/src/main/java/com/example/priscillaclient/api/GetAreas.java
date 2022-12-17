package com.example.priscillaclient.api;

import android.content.Context;
import android.os.AsyncTask;

import com.example.priscillaclient.AreaActivity;
import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.models.Area;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class GetAreas extends AsyncTask<String, String, ArrayList<Area>> {

    final Context context;
    final int categoryId;

    public GetAreas(Context context, int categoryId) {
        super();
        this.context = context;
        this.categoryId = categoryId;
    }

    @Override
    protected ArrayList<Area> doInBackground(String... strings) {

        try {
            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/get-areas/" + categoryId, "GET", false);

            int status = connection.getResponseCode();
            String message = connection.getResponseMessage();

            InputStream responseStream = connection.getInputStream();

            String responseStr;
            try (Scanner scanner = new Scanner(responseStream)) {
                responseStr = scanner.useDelimiter("\\A").next();
            }


            JSONArray json = new JSONObject(responseStr).getJSONArray("areas");

            Client client = Client.getInstance();

            client.areas = new ArrayList<>();

            for (int i = 0; i < json.length(); ++i) {
                Area a = new Area(json.getJSONObject(i));
                client.areas.add(a);
            }

            return client.areas;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Area> areas) {
        ((AreaActivity) context).onUpdate(areas);
    }
}
