package com.example.priscillaclient;

import com.example.priscillaclient.models.Chapter;
import com.example.priscillaclient.models.Course;
import com.example.priscillaclient.models.Language;
import com.example.priscillaclient.models.Lesson;
import com.example.priscillaclient.models.Task;

import java.util.ArrayList;

public class Client {

    // TODO rename to cache

    // Client data

    public String token_type = "";
    public long expires_in = 0;
    public long logged_in = 0;
    public String access_token = "";
    public String refresh_token = "";

    public User user = null;

    public ArrayList<Course> courses = null;

    public ArrayList<Chapter> chapters = null;

    public ArrayList<Lesson> lessons = null;

    public ArrayList<Task> tasks = null;

    public ArrayList<Language> languageList = null;

    public boolean hasValidToken() {
        return !(token_type.isEmpty() || access_token.isEmpty() || isTokenExpired());
    }

    public boolean isTokenExpired() {
        long currentTime = System.currentTimeMillis() / 1000;
        return (currentTime > logged_in + expires_in);
    }

    // Singleton requirements

    private static Client instance = null;

    private Client() { }

    public static Client getInstance() {

        if (instance == null) {
            instance = new Client();
        }

        return instance;
    }
}
