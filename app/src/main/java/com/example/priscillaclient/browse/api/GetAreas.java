package com.example.priscillaclient.browse.api;

import com.example.priscillaclient.util.HttpConnection;
import com.example.priscillaclient.browse.viewmodel.models.Area;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GetAreas implements Callable<ArrayList<Area>> {

    private final int categoryId;

    public GetAreas(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public ArrayList<Area> call() throws Exception {
        HttpConnection connection = new HttpConnection("/get-areas/" + categoryId, "GET");

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONArray json = new JSONObject(connection.getResponse()).getJSONArray("areas");

        ArrayList<Area> areas = new ArrayList<>();
        for (int i = 0; i < json.length(); ++i) {
            Area a = new Area(json.getJSONObject(i));
            areas.add(a);
        }

        return areas;
    }
}
