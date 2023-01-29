package com.example.priscillaclient.viewmodels.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.GetCourses;
import com.example.priscillaclient.viewmodels.app.models.Course;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;

public class CoursesViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<Course>> state = new MutableLiveData<>(null);

    public LiveData<ArrayList<Course>> getData() {
        return state;
    }

    public void fetchData() {
        if (getData().getValue() == null || getData().getValue().isEmpty()) {
            state.setValue(new ArrayList<>());
            apiTask.executeAsync(new GetCourses(), (data, error) -> {
                setError(error);
                state.setValue(data);
            });
        } else {
            clear();
        }
    }

    public void clear() {
        state.setValue(null);
    }
}
