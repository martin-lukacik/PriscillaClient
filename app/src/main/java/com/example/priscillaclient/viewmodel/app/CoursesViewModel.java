package com.example.priscillaclient.viewmodel.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.app.GetCourses;
import com.example.priscillaclient.models.Course;
import com.example.priscillaclient.viewmodel.ViewModelBase;

import java.util.ArrayList;

public class CoursesViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<Course>> state = new MutableLiveData<>(new ArrayList<>());

    public LiveData<ArrayList<Course>> getData() {
        return state;
    }

    public void fetchData() {
        if (getData().getValue().isEmpty()) {
            apiTask.executeAsync(new GetCourses(), (data, error) -> {
                setError(error);
                state.setValue(data);
            });
        }
    }
}