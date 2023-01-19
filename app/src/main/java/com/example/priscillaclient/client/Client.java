package com.example.priscillaclient.client;

import com.example.priscillaclient.models.Area;
import com.example.priscillaclient.models.AreaCourse;
import com.example.priscillaclient.models.Category;
import com.example.priscillaclient.models.Chapter;
import com.example.priscillaclient.models.Course;
import com.example.priscillaclient.models.Language;
import com.example.priscillaclient.models.Lesson;
import com.example.priscillaclient.models.Task;
import com.example.priscillaclient.models.User;

import java.util.ArrayList;

public class Client {

    // Client data

    public String token_type = "";
    public long expires_in = 0;
    public long logged_in = 0;
    public String access_token = "";
    public String refresh_token = "";

    public User user = null;

    public ArrayList<Course> courses = new ArrayList<>();

    public ArrayList<Chapter> chapters = new ArrayList<>();
    public int lastCourseId = -1;

    public ArrayList<Lesson> lessons = new ArrayList<>();
    public int lastChapterId = -1;

    public ArrayList<Task> tasks = new ArrayList<>();
    public int lastLessonId = -1;

    public ArrayList<Language> languageList = null;
    public ArrayList<Category> categories = null;
    public ArrayList<Area> areas = null;
    public ArrayList<AreaCourse> areaCourses = null;

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
