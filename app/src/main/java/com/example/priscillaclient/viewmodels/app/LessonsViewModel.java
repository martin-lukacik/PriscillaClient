package com.example.priscillaclient.viewmodels.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.GetLessons;
import com.example.priscillaclient.viewmodels.app.models.Lesson;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;

public class LessonsViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<Lesson>> state = new MutableLiveData<>(new ArrayList<>());

    private int lastChapterId = -1;

    public LiveData<ArrayList<Lesson>> getData() {
        return state;
    }

    public void fetchData(int chapterId) {
        if (lastChapterId != chapterId) {
            lastChapterId = chapterId;

            setLoadingState(true);
            apiTask.executeAsync(new GetLessons(chapterId), (data, error) -> {
                setLoadingState(false);
                if (error != null)
                    setErrorState(error);
                else
                    state.setValue(data);
            });
        }
    }

    public void clear() {
        state.setValue(new ArrayList<>());
    }
}
