package com.example.priscillaclient.api.misc;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.viewmodels.misc.models.Leader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GetLeaders implements Callable<ArrayList<Leader>> {

    @Override
    public ArrayList<Leader> call() throws Exception {

        HttpConnection connection = new HttpConnection("/get-leaders2", "POST");

        JSONObject json = getJSONObject();

        connection.sendRequest(json);

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONArray response = new JSONObject(connection.getResponse()).getJSONArray("list");

        ArrayList<Leader> leaderboard = new ArrayList<>();
        for (int i = 0; i < response.length(); ++i) {
            leaderboard.add(new Leader(response.getJSONObject(i)));
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
