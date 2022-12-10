package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Area {
    int id;
    String area_name;
    String area_color;
    String area_icon;

    int category_id;
    int area_order;
    int number_of_courses;

    public Area(JSONObject json) throws JSONException {
        id = json.getInt("id");
        area_name = json.getString("area_name");
        area_color = json.getString("area_color");
        area_icon = json.getString("area_icon");
        category_id = json.getInt("category_id");
        area_order = json.getInt("area_order");
        number_of_courses = json.getInt("number_of_courses");
    }

    @Override
    public String toString() {
        return area_name;
    }

}
