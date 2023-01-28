package com.example.priscillaclient.viewmodel.browse.models;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class Area {
    public final int id;
    public final String area_name;
    public final String area_color;
    public final String area_icon;

    public final int category_id;
    public final int area_order;
    public final int number_of_courses;

    public Area(JSONObject json) throws JSONException {
        id = json.getInt("id");
        area_name = json.getString("area_name");
        area_color = json.getString("area_color");
        area_icon = json.getString("area_icon");
        category_id = json.getInt("category_id");
        area_order = json.getInt("area_order");
        number_of_courses = json.getInt("number_of_courses");
    }

    @NotNull
    @Override
    public String toString() {
        return area_name;
    }

}
