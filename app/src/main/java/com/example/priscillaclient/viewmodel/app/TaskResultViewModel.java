package com.example.priscillaclient.viewmodel.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.viewmodel.app.models.TaskResult;
import com.example.priscillaclient.viewmodel.ViewModelBase;

import java.util.concurrent.Callable;

public class TaskResultViewModel extends ViewModelBase {
    private MutableLiveData<TaskResult> state = new MutableLiveData<>(null);

    public LiveData<TaskResult> getData() {
        return state;
    }

    public void postData(Callable<TaskResult> callable) {
        apiTask.executeAsync(callable, (data, error) -> {
            setError(error);
            state.setValue(data);
        });
    }

    public void clear() {
        state = new MutableLiveData<>(null);
    }
}
