package com.example.priscillaclient.models.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.GetCourseList;
import com.example.priscillaclient.models.Course;

import java.util.ArrayList;

public class CoursesViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<Course>> state = new MutableLiveData<>(new ArrayList<>());

    public LiveData<ArrayList<Course>> getData() {
        return state;
    }

    public void fetchData() {
        apiTask.executeAsync(new GetCourseList(), (data, error) -> {
            setError(error);
            state.setValue(data);
        });
    }
}
