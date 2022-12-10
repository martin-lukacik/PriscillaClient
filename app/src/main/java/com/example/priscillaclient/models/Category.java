package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Category {

    String title;
    public int category_id;
    int sort_order;
    String color;

    Tuple areas, courses, chapters, lessons, codes;

    public class Tuple {
        public final String text;
        public final int value;

        public Tuple(JSONObject json) throws JSONException {
            text = json.getString("text");
            value = json.getInt("value");
        }
    }

    public Category(JSONObject json) throws JSONException {
        title = json.getString("title");
        category_id = json.getInt("category_id");
        sort_order = json.getInt("sort_order");
        color = json.getString("color");

        areas = new Tuple(json.getJSONObject("areas"));
        courses = new Tuple(json.getJSONObject("courses"));
        chapters = new Tuple(json.getJSONObject("chapters"));
        lessons = new Tuple(json.getJSONObject("lessons"));
        codes = new Tuple(json.getJSONObject("codes"));
    }

    @Override
    public String toString() {
        return title;
    }
}
