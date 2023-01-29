package com.example.priscillaclient.viewmodels.app.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Help {

    public final String help;
    public final int updated_xp;
    public final int updated_coins;
    public final int updated_level;

    public Help(JSONObject json) throws JSONException {
        help = json.getString("help");

        JSONObject user = json.getJSONObject("user");
        updated_xp = user.getInt("xp");
        updated_coins = user.getInt("coins");
        updated_level = user.getInt("level");
    }
}
