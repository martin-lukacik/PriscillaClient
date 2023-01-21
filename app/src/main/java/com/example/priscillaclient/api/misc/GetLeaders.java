package com.example.priscillaclient.api.misc;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.example.priscillaclient.api.ApiTask;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.LeaderboardItem;
import com.example.priscillaclient.views.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetLeaders extends ApiTask {

    public GetLeaders(HttpResponse context) {
        super(context);

        Activity ctx = (context instanceof Activity ? (Activity) context : ((Fragment) context).getActivity());
        dialog = new LoadingDialog(ctx, "Loading, please wait...");
        dialog.show();
    }

    @Override
    protected ArrayList<LeaderboardItem> doInBackground(String... strings) {

        if (!client.leaderboard.isEmpty()) {
            return client.leaderboard;
        }

        try {
            HttpConnection connection = new HttpConnection("/get-leaders2", "POST", true);

            JSONObject json = getJSONObject();

            connection.sendRequest(json);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorStream());
                return client.leaderboard;
            }

            JSONArray response = new JSONObject(connection.getResponse()).getJSONArray("list");

            client.leaderboard.clear();
            for (int i = 0; i < response.length(); ++i) {
                client.leaderboard.add(new LeaderboardItem(response.getJSONObject(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return client.leaderboard;
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