package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class LeaderboardItem {
    public int id;
    public String nickname;
    public String groups;
    public int xp;
    public int level_id;
    public String country;

    public LeaderboardItem(JSONObject json) throws JSONException {
        id = json.getInt("id");
        nickname = json.getString("nickname");
        groups = json.getString("groups");
        xp = json.getInt("xp");
        level_id = json.getInt("level_id");
        country = json.getString("country");
    }
}
