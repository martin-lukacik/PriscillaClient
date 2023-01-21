package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile {

    public String name;
    public String surname;
    public String nickname;
    public String groups;
    public int yob;
    public int xp;
    public int level_id;
    public int id;
    public String email;
    public int pref_lang_id;
    public int content_type_id;
    public int country_id;
    public int role_id;
    public int theme_id;

    public Profile(JSONObject json) throws JSONException {
        name = json.getString("name");
        surname = json.getString("surname");
        nickname = json.getString("nickname");
        groups = json.getString("groups");
        yob = json.getInt("yob");
        xp = json.getInt("xp");
        level_id = json.getInt("level_id");
        id = json.getInt("id");
        email = json.getString("email");
        pref_lang_id = json.getInt("pref_lang_id");
        content_type_id = json.getInt("content_type_id");
        country_id = json.getInt("country_id");
        role_id = json.getInt("role_id");
        theme_id = json.getInt("theme_id");
    }
}
