package com.example.priscillaclient.leaderboard.api;

import com.example.priscillaclient.util.HttpConnection;
import com.example.priscillaclient.leaderboard.viewmodel.models.LeaderboardItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GetLeaders implements Callable<ArrayList<LeaderboardItem>> {

    @Override
    public ArrayList<LeaderboardItem> call() throws Exception {

        HttpConnection connection = new HttpConnection("/get-leaders2", "POST");

        JSONObject json = getJSONObject();

        connection.sendRequest(json);

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONArray response = new JSONObject(connection.getResponse()).getJSONArray("list");

        ArrayList<LeaderboardItem> leaderboard = new ArrayList<>();
        for (int i = 0; i < response.length(); ++i) {
            leaderboard.add(new LeaderboardItem(response.getJSONObject(i)));
        }

        return leaderboard;
    }

    private JSONObject getJSONObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("areas", "");
        json.put("countries", "");
        json.put("courses", "");
        json.put("groups", "");
        json.put("start", 0);
        json.put("end", 100);
        return json;
    }
}
