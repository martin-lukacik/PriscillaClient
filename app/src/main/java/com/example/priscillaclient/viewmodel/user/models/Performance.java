package com.example.priscillaclient.viewmodel.user.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Performance {
    public final int xp;
    public final int coins;
    public final int level;

    public Performance(JSONObject json) throws JSONException {
        xp = json.getInt("xp");
        coins = json.getInt("coins");
        level = json.getInt("level");
    }
}
