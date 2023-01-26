package com.example.priscillaclient.viewmodel.user.models;

import com.example.priscillaclient.models.Performance;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public final int user_id;
    public final int lang_id;
    public final int country_id;
    public final int role_id;
    public final int content_type_id;

    public final int theme_id;
    public final String theme_value;
    public final String name;
    public final String surname;
    public final String email;

    public final Performance performance;

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
