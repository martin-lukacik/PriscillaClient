package com.example.priscillaclient.api.browse;

import com.example.priscillaclient.api.ApiTaskLegacy;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.AreaCourse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetAreaCourses extends ApiTaskLegacy {

    final int areaId;

    public GetAreaCourses(HttpResponse context, int areaId) {
        super(context);
        this.areaId = areaId;
    }

    @Override
    protected ArrayList<AreaCourse> doInBackground(String... strings) {

        // Return from cache
        if (client.lastAreaId == areaId) {
            if (!client.areaCourses.isEmpty())
                return client.areaCourses;
        }
        client.lastAreaId = areaId;

        try {
            HttpConnection connection = new HttpConnection("/area-all-courses/" + areaId, "GET", false);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorMessage());
                return client.areaCourses;
            }

            JSONArray json = new JSONObject(connection.getResponse()).getJSONArray("list");

            client.areaCourses.clear();
            for (int i = 0; i < json.length(); ++i) {
                AreaCourse a = new AreaCourse(json.getJSONObject(i));
                client.areaCourses.add(a);
            }

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return client.areaCourses;
    }
}

