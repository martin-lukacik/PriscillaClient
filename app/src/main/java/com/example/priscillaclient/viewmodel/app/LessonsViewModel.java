package com.example.priscillaclient.viewmodel.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.app.GetLessons;
import com.example.priscillaclient.viewmodel.app.models.Lesson;
import com.example.priscillaclient.viewmodel.ViewModelBase;

import java.util.ArrayList;

public class LessonsViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<Lesson>> state = new MutableLiveData<>(new ArrayList<>());

    private int lastChapterId = -1;

    public LiveData<ArrayList<Lesson>> getData() {
        return state;
    }

    public void fetchData(int chapterId) {
        if (lastChapterId != chapterId) {
            apiTask.executeAsync(new GetLessons(chapterId), (data, error) -> {
                setError(error);
                state.setValue(data);
            });
            lastChapterId = chapterId;
        }
    }
}
