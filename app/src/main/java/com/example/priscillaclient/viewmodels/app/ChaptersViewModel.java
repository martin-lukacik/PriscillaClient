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
            lastCourseId = courseId;

            clear();
            setLoadingState(true);
            apiTask.executeAsync(new GetChapters(courseId), (data, error) -> {
                setLoadingState(false);
                if (error != null)
                    setErrorState(error);
                else
                    state.setValue(data);
            });
        }
    }

    public void clear() {
        state.setValue(null);
    }
}
