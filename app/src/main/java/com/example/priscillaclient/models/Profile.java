package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile {

    public final String name;
    public final String surname;
    public final String nickname;
    public final String groups;
    public final int yob;
    public final int xp;
    public final int level_id;
    public final int id;
    public final String email;
    public final int pref_lang_id;
    public final int content_type_id;
    public final int country_id;
    public final int role_id;
    public final int theme_id;

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
