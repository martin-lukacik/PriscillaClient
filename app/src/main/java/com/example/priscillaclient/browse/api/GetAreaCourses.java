package com.example.priscillaclient.browse.api;

import com.example.priscillaclient.util.HttpConnection;
import com.example.priscillaclient.browse.viewmodel.models.AreaCourse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GetAreaCourses implements Callable<ArrayList<AreaCourse>> {

    private final int areaId;

    public GetAreaCourses(int areaId) {
        this.areaId = areaId;
    }

    @Override
    public ArrayList<AreaCourse> call() throws Exception {
        HttpConnection connection = new HttpConnection("/area-all-courses/" + areaId, "GET");

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONArray json = new JSONObject(connection.getResponse()).getJSONArray("list");

        ArrayList<AreaCourse> areaCourses = new ArrayList<>();
        for (int i = 0; i < json.length(); ++i) {
            AreaCourse a = new AreaCourse(json.getJSONObject(i));
            areaCourses.add(a);
        }

        return areaCourses;
    }
}
