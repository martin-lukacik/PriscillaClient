package com.example.priscillaclient.api.browse;

import com.example.priscillaclient.api.ApiTaskLegacy;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.Area;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetAreas extends ApiTaskLegacy {

    final int categoryId;

    public GetAreas(HttpResponse context, int categoryId) {
        super(context);
        this.categoryId = categoryId;
    }

    @Override
    protected ArrayList<Area> doInBackground(String... strings) {

        // Return from cache
        if (client.lastCategoryId == categoryId) {
            if (!client.areas.isEmpty())
                return client.areas;
        }
        client.lastCategoryId = categoryId;

        try {
            HttpConnection connection = new HttpConnection("/get-areas/" + categoryId, "GET");

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorMessage());
                return client.areas;
            }

            JSONArray json = new JSONObject(connection.getResponse()).getJSONArray("areas");

            client.areas.clear();
            for (int i = 0; i < json.length(); ++i) {
                Area a = new Area(json.getJSONObject(i));
                client.areas.add(a);
            }

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return client.areas;
    }
}
