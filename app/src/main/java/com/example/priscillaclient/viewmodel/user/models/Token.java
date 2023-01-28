package com.example.priscillaclient.viewmodel.user.models;

import com.example.priscillaclient.util.Client;

import org.json.JSONException;
import org.json.JSONObject;

public class Token {

    public final String token_type;
    public final long expires_in;
    public final long logged_in;
    public final String access_token;
    public final String refresh_token;

    public Token(JSONObject json) throws JSONException {
        token_type = json.getString("token_type");
        expires_in = json.getInt("expires_in");
        access_token = json.getString("access_token");
        refresh_token = json.getString("refresh_token");
        logged_in = System.currentTimeMillis() / 1000;

        Client.getInstance().token = this;
    }
}
