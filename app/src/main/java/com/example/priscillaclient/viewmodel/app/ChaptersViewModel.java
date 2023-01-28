package com.example.priscillaclient.viewmodel.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.GetChapters;
import com.example.priscillaclient.viewmodel.app.models.Chapter;
import com.example.priscillaclient.viewmodel.ViewModelBase;

import java.util.ArrayList;

public class ChaptersViewModel extends ViewModelBase {

    private final MutableLiveData<ArrayList<Chapter>> state = new MutableLiveData<>(null);

    private int lastCourseId = -1;

    public int getLastCourseId() {
        return lastCourseId;
    }

    public LiveData<ArrayList<Chapter>> getData() {
        return state;
    }

    public void fetchData(int courseId) {
        if (lastCourseId != courseId) {
            apiTask.executeAsync(new GetChapters(courseId), (data, error) -> {
                setError(error);
                state.setValue(data);
            });
            lastCourseId = courseId;
        }
    }
}
