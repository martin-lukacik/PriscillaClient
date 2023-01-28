package com.example.priscillaclient.viewmodel.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.GetTasks;
import com.example.priscillaclient.viewmodel.app.models.Task;
import com.example.priscillaclient.viewmodel.ViewModelBase;

import java.util.ArrayList;

public class TasksViewModel extends ViewModelBase {

    private final MutableLiveData<ArrayList<Task>> state = new MutableLiveData<>(new ArrayList<>());

    private int lastLessonId = -1;

    public LiveData<ArrayList<Task>> getData() {
        return state;
    }

    public void fetchData(int courseId, int chapterId, int lessonId, boolean forceFetch) {
        if (forceFetch || lastLessonId != lessonId) {
            apiTask.executeAsync(new GetTasks(courseId, chapterId, lessonId), (data, error) -> {
                setError(error);
                state.setValue(data);
            });
            lastLessonId = lessonId;
        }
    }
}
