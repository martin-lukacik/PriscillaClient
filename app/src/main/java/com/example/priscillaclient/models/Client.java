package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Client {

    // Client data

    public String token_type = "";
    public long expires_in = 0;
    public long logged_in = 0;
    public String access_token = "";
    public String refresh_token = "";

    public User user = null;
    public RegistrationData registrationData = new RegistrationData();
    public Profile profile = null;

    public ArrayList<Course> courses = new ArrayList<>();
    public ArrayList<Chapter> chapters = new ArrayList<>();
    public int lastCourseId = -1;
    public ArrayList<Lesson> lessons = new ArrayList<>();
    public int lastChapterId = -1;
    public ArrayList<Task> tasks = new ArrayList<>();
    public int lastLessonId = -1;

    public ArrayList<Category> categories = new ArrayList<>();
    public ArrayList<Area> areas = new ArrayList<>();
    public int lastCategoryId = -1;
    public ArrayList<AreaCourse> areaCourses = new ArrayList<>();
    public int lastAreaId = -1;

    public ArrayList<LeaderboardItem> leaderboard = new ArrayList<>();


    private static Client instance = null;

    private Client() { }

    public static Client getInstance() {

        if (instance == null) {
            instance = new Client();
        }

        return instance;
    }

    public static void set(JSONObject json) throws JSONException {
        getInstance().token_type = json.getString("token_type");
        getInstance().expires_in = json.getInt("expires_in");
        getInstance().access_token = json.getString("access_token");
        getInstance().refresh_token = json.getString("refresh_token");
        getInstance().logged_in = System.currentTimeMillis() / 1000;
    }

    public boolean hasValidToken() {
        return !(token_type.isEmpty() || access_token.isEmpty() || isTokenExpired());
    }

    public boolean isTokenExpired() {
        long currentTime = System.currentTimeMillis() / 1000;
        return (currentTime > logged_in + expires_in);
    }
}
