package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    int user_id;
    int lang_id;
    int country_id;
    int role_id;
    int content_type_id;

    int theme_id;
    String theme_value;
    public String name;
    public String surname;
    public String email;

    public Performance performance;

    public static class Performance {
        public int xp;
        public int coins;
        public int level;

        public Performance(JSONObject json) throws JSONException {
            xp = json.getInt("xp");
            coins = json.getInt("coins");
            level = json.getInt("level");
        }
    }

    public User(JSONObject json) throws JSONException {
        user_id = json.getInt("user_id");
        lang_id = json.getInt("lang_id");
        country_id = json.getInt("country_id");
        name = json.getString("name");
        surname = json.getString("surname");
        email = json.getString("email");
        role_id = json.getInt("role_id");
        content_type_id = json.getInt("content_type_id");
        theme_id = json.getInt("theme_id");
        theme_value = json.getString("theme_value");

        performance = new Performance(json.getJSONObject("performance"));
    }
}
