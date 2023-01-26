package com.example.priscillaclient.user.viewmodel.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Language {
    public final int id;
    public final String name;
    public final String shortcut;

    public Language(JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        shortcut = json.getString("shortcut");
    }
}
