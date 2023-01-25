package com.example.priscillaclient.models.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.GetChapterList;
import com.example.priscillaclient.models.Chapter;

import java.util.ArrayList;

public class ChaptersViewModel extends ViewModelBase {

    private final MutableLiveData<ArrayList<Chapter>> state = new MutableLiveData<>(new ArrayList<>());

    private int lastCourseId = -1;

    public LiveData<ArrayList<Chapter>> getData() {
        return state;
    }

    public void fetchData(int courseId) {
        apiTask.executeAsync(new GetChapterList(courseId), (data, error) -> {
            setError(error);
            state.setValue(data);
        });
    }
}
