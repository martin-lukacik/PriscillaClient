package com.example.priscillaclient.user.viewmodel.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Theme {
    public final int id;
    public final String group_name;
    public final String theme_name;
    public final String description;

    public Theme(JSONObject json) throws JSONException {
        id = json.getInt("id");
        group_name = json.getString("group_name");
        theme_name = json.getString("theme_name");
        description = json.getString("description");
    }
}
