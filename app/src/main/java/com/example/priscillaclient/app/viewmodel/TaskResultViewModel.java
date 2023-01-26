package com.example.priscillaclient.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.app.viewmodel.models.TaskResult;
import com.example.priscillaclient.util.ViewModelBase;

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
