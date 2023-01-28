package com.example.priscillaclient.util;

import com.example.priscillaclient.viewmodel.user.models.Token;

public class Client {

    // Client data

    public static final String baseUrl = "https://app.priscilla.fitped.eu";

    public Token token = null;

    private static Client instance = null;

    private Client() { }

    public static Client getInstance() {

        if (instance == null) {
            instance = new Client();
        }

        return instance;
    }

    public boolean hasValidToken() {
        return token != null && !(token.token_type.isEmpty() || token.access_token.isEmpty() || isTokenExpired());
    }

    public boolean isTokenExpired() {
        long currentTime = System.currentTimeMillis() / 1000;
        return token != null && (currentTime > token.logged_in + token.expires_in);
    }
}
