package com.example.priscillaclient.viewmodels.user.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Theme {

    public static final int THEME_NONE = 0;
    public static final int THEME_LIGHT = 1;
    public static final int THEME_DARK = 2;
    public static final int THEME_COLORBLIND = 3;
    public static final int THEME_FOLLOW_SYSTEM = 4;

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
