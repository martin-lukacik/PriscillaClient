package com.example.priscillaclient.viewmodels.user.models;

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

    public Profile() {
        this(new JSONObject());
    }

    public Profile(JSONObject json) {
        name = json.optString("name");
        surname = json.optString("surname");
        nickname = json.optString("nickname");
        groups = json.optString("groups");
        yob = json.optInt("yob");
        xp = json.optInt("xp");
        level_id = json.optInt("level_id");
        id = json.optInt("id");
        email = json.optString("email");
        pref_lang_id = json.optInt("pref_lang_id");
        content_type_id = json.optInt("content_type_id");
        country_id = json.optInt("country_id");
        role_id = json.optInt("role_id");
        theme_id = json.optInt("theme_id");
    }
}
