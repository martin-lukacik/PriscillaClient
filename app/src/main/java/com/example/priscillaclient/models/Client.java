package com.example.priscillaclient.models;

import com.example.priscillaclient.viewmodel.user.models.Profile;
import com.example.priscillaclient.viewmodel.user.models.Settings;
import com.example.priscillaclient.viewmodel.user.models.Token;
import com.example.priscillaclient.viewmodel.user.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Client {

    // Client data

    public Token token = null;

    public final ArrayList<Category> categories = new ArrayList<>();
    public final ArrayList<Area> areas = new ArrayList<>();
    public int lastCategoryId = -1;
    public final ArrayList<AreaCourse> areaCourses = new ArrayList<>();
    public int lastAreaId = -1;

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
