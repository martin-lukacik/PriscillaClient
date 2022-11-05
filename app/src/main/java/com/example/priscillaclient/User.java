package com.example.priscillaclient;

import com.example.priscillaclient.models.Course;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    int user_id;
    int lang_id;
    int country_id;
    int role_id;
    int content_type_id;

    int theme_id;
    String theme_value;
    String name;
    String surname;
    String email;

    public Performance performance;

    class Performance {
        int xp;
        int coins;
        int level;
        // TODO badges

        public Performance(JSONObject json) throws JSONException {
            xp = json.getInt("xp");
            coins = json.getInt("coins");
            level = json.getInt("level");
            // TODO badges
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
