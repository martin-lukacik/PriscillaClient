package com.example.priscillaclient.models;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class Category {

    public final String title;
    public final int category_id;
    public final int sort_order;
    public final String color;

    public final Pair areas, courses, chapters, lessons, codes;

    public static class Pair {
        public final String text;
        public final int value;

        public Pair(JSONObject json) throws JSONException {
            text = json.getString("text");
            value = json.getInt("value");
        }
    }

    public Category(JSONObject json) throws JSONException {
        title = json.getString("title");
        category_id = json.getInt("category_id");
        sort_order = json.getInt("sort_order");
        color = json.getString("color");

        areas = new Pair(json.getJSONObject("areas"));
        courses = new Pair(json.getJSONObject("courses"));
        chapters = new Pair(json.getJSONObject("chapters"));
        lessons = new Pair(json.getJSONObject("lessons"));
        codes = new Pair(json.getJSONObject("codes"));
    }

    @NotNull
    @Override
    public String toString() {
        return title;
    }
}
