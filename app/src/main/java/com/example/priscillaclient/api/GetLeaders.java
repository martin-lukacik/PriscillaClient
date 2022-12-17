package com.example.priscillaclient.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.LeaderboardActivity;
import com.example.priscillaclient.models.LeaderboardItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class GetLeaders extends AsyncTask<String, String, ArrayList<LeaderboardItem>> {

    final Context context;
    public GetLeaders(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected ArrayList<LeaderboardItem> doInBackground(String... strings) {

        ArrayList<LeaderboardItem> leaders = new ArrayList<>();

        try {
            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/get-leaders2", "POST", true);

            JSONObject j = new JSONObject();
            j.put("areas", "");
            j.put("countries", "");
            j.put("courses", "");
            j.put("groups", "");
            j.put("start", 0);
            j.put("end", 100);

            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(j.toString());

            os.flush();
            os.close();

            int status = connection.getResponseCode();
            String message = connection.getResponseMessage();

            InputStream responseStream = connection.getInputStream();

            String responseStr;
            try (Scanner scanner = new Scanner(responseStream)) {
                responseStr = scanner.useDelimiter("\\A").next();
            }

            Log.i("LEADERS", responseStr);

            JSONArray json = new JSONObject(responseStr).getJSONArray("list");

            for (int i = 0; i < json.length(); ++i) {
                leaders.add(new LeaderboardItem(json.getJSONObject(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return leaders;
    }

    protected void onPostExecute(ArrayList<LeaderboardItem> leaders) {
        ((LeaderboardActivity) context).onUpdate(leaders);
    }

}
