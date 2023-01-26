package com.example.priscillaclient.user.viewmodel.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Group {
    public final int id;
    public final String group_name;

    public Group(JSONObject json) throws JSONException {
        id = json.getInt("id");
        group_name = json.getString("group_name");
    }
}
