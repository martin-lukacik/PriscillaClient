package com.example.priscillaclient.viewmodels.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.GetChapters;
import com.example.priscillaclient.viewmodels.app.models.Chapter;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;

public class ChaptersViewModel extends ViewModelBase {

    private final MutableLiveData<ArrayList<Chapter>> state = new MutableLiveData<>(null);

    private int lastCourseId = -1;

    public LiveData<ArrayList<Chapter>> getData() {
        return state;
    }

    public void fetchData(int courseId) {
        if (lastCourseId != courseId) {
            state.setValue(null);
            apiTask.executeAsync(new GetChapters(courseId), (data, error) -> {
                setError(error);
                state.setValue(data);
            });
            lastCourseId = courseId;
        }
    }

    public void clear() {
        state.setValue(null);
    }
}
