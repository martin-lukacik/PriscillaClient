package com.example.priscillaclient.viewmodels.misc.models;

import org.json.JSONException;
import org.json.JSONObject;

public class LeaderboardItem {
    public final int id;
    public final String nickname;
    public final String groups;
    public final int xp;
    public final int level_id;
    public final String country;

    public LeaderboardItem(JSONObject json) throws JSONException {
        id = json.getInt("id");
        nickname = json.getString("nickname");
        groups = json.getString("groups");
        xp = json.getInt("xp");
        level_id = json.getInt("level_id");
        country = json.getString("country");
    }
}
