package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Language {
    public int id;
    public String name;
    public String shortcut;

    public Language(JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        shortcut = json.getString("shortcut");
    }
}
