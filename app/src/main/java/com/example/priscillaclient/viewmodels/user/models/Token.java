package com.example.priscillaclient.viewmodels.user.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Token {

    public final String token_type;
    public final long expires_in;
    public final long logged_in;
    public final String access_token;
    public final String refresh_token;

    private static Token token;

    private Token(JSONObject json) throws JSONException {
        token_type = json.getString("token_type");
        expires_in = json.getInt("expires_in");
        access_token = json.getString("access_token");
        refresh_token = json.getString("refresh_token");
        logged_in = System.currentTimeMillis() / 1000;
    }

    public static void set(JSONObject json) throws JSONException {
        token = new Token(json);
    }

    public static Token get() {
        return token;
    }

    public static boolean isValid() {
        return token != null && !(token.token_type.isEmpty() || token.access_token.isEmpty() || isExpired());
    }

    public static boolean isExpired() {
        long currentTime = System.currentTimeMillis() / 1000;
        return token != null && (currentTime > token.logged_in + token.expires_in);
    }
}
