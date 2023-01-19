package com.example.priscillaclient.api;

import android.os.AsyncTask;

import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.models.Course;

import java.util.ArrayList;

public abstract class ApiTask extends AsyncTask<String, String, Object> {

    FragmentBase fragment;

    public ApiTask(FragmentBase fragment) {
        super();
        this.fragment = fragment;
    }

    protected void onPostExecute(ArrayList<Course> courses) {
        fragment.onUpdate(courses);
    }
}
