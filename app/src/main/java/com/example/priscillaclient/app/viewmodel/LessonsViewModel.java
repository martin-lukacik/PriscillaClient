package com.example.priscillaclient.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.app.api.GetLessons;
import com.example.priscillaclient.app.viewmodel.models.Lesson;
import com.example.priscillaclient.util.ViewModelBase;

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
