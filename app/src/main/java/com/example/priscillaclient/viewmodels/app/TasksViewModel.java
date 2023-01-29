package com.example.priscillaclient.viewmodels.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.GetTasks;
import com.example.priscillaclient.api.tasks.GetHelp;
import com.example.priscillaclient.viewmodels.app.models.Help;
import com.example.priscillaclient.viewmodels.app.models.Task;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;

public class TasksViewModel extends ViewModelBase {

    private final MutableLiveData<ArrayList<Task>> state = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Help> helpState = new MutableLiveData<>(null);

    private int lastLessonId = -1;

    public LiveData<ArrayList<Task>> getData() {
        return state;
    }

    public LiveData<Help> getHelpState() {
        return helpState;
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

    public void getHelp(int taskId) {
        apiTask.executeAsync(new GetHelp(taskId), (data, error) -> {
            setError(error);
            helpState.setValue(data);
            helpState.setValue(null); // clear help, no longer needed
        });
    }

    public void clear() {
        state.setValue(new ArrayList<>());
    }
}
