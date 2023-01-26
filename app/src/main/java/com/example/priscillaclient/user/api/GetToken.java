package com.example.priscillaclient.user.api;

import com.example.priscillaclient.util.HttpConnection;
import com.example.priscillaclient.user.viewmodel.models.Token;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class GetToken implements Callable<Token> {

    public static final int client_id = 2;
    public static final String client_secret = "iQuGUAzqc187j7IKQ94tTVJAywHCAzYBGAMTxEtr";

    private final String username;
    private final String password;
    private final String email;
    private final String grantType;

    public GetToken(String username, String password, String email, String grantType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.grantType = grantType;
    }

    @Override
    public Token call() throws Exception {
        HttpConnection connection = new HttpConnection("/oauth/token", "POST");

        JSONObject json = new JSONObject();
        json.put("client_id", client_id);
        json.put("client_secret", client_secret);
        json.put("email", email);
        json.put("grant_type", grantType);
        json.put(grantType, password);
        json.put("username", username);

        connection.sendRequest(json);

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONObject response = new JSONObject(connection.getResponse());

        return new Token(response);
    }
}