package com.example.priscillaclient.viewmodels.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.DoJoinCourse;
import com.example.priscillaclient.api.browse.GetCourses;
import com.example.priscillaclient.viewmodels.app.models.Course;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;

public class CoursesViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<Course>> state = new MutableLiveData<>(null);

    private final MutableLiveData<String> joinCourseState = new MutableLiveData<>(null);

    public LiveData<ArrayList<Course>> getData() {
        return state;
    }

    public LiveData<String> getJoinState() {
        return joinCourseState;
    }

    public void fetchData(boolean forceFetch) {
        if (forceFetch || getData().getValue() == null || getData().getValue().isEmpty()) {
            clear();
            setLoadingState(true);
            apiTask.executeAsync(new GetCourses(), (data, error) -> {
                setLoadingState(false);
                if (error != null)
                    setErrorState(error);
                else
                    state.setValue(data);
            });
        }
    }

    public void joinCourse(int courseId) {
        apiTask.executeAsync(new DoJoinCourse(courseId), (data, error) -> {
            if (error != null)
                setErrorState(error);
            else
                joinCourseState.setValue(data);
        });
    }

    public void clear() {
        state.setValue(null);
    }
}
