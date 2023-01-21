package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Theme {
    public int id;
    public String group_name;
    public String theme_name;
    public String description;

    public Theme(JSONObject json) throws JSONException {
        id = json.getInt("id");
        group_name = json.getString("group_name");
        theme_name = json.getString("theme_name");
        description = json.getString("description");
    }
}
