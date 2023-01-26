package com.example.priscillaclient.viewmodel.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.models.TaskResult;
import com.example.priscillaclient.viewmodel.ViewModelBase;

import java.util.concurrent.Callable;

public class TaskResultViewModel extends ViewModelBase {
    private final MutableLiveData<TaskResult> state = new MutableLiveData<>(null);

    public LiveData<TaskResult> getData() {
        return state;
    }

    public void postData(Callable<TaskResult> callable) {
        apiTask.executeAsync(callable, (data, error) -> {
            setError(error);
            state.setValue(data);
        });
    }
}
