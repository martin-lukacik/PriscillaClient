package com.example.priscillaclient.viewmodels.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.app.DoSaveProgram;
import com.example.priscillaclient.api.app.GetProgram;
import com.example.priscillaclient.viewmodels.app.models.TaskResult;
import com.example.priscillaclient.misc.Pair;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TaskResultViewModel extends ViewModelBase {
    private MutableLiveData<TaskResult> state = new MutableLiveData<>(null);
    private final MutableLiveData<String> saveState = new MutableLiveData<>(null);
    private final MutableLiveData<Pair<ArrayList<String>, ArrayList<String>>> code = new MutableLiveData<>(null);

    public LiveData<TaskResult> getData() {
        return state;
    }

    public LiveData<String> getSaveState() {
        return saveState;
    }

    public void postData(Callable<TaskResult> callable) {
        setLoadingState(true);
        apiTask.executeAsync(callable, (data, error) -> {
            setLoadingState(false);
            if (error != null)
                setErrorState(error);
            else
                state.setValue(data);
        });
    }

    public void saveCode(int taskId, ArrayList<String> fileNames, ArrayList<String> fileContents) {
        apiTask.executeAsync(new DoSaveProgram(taskId, fileNames, fileContents), (data, error) -> {
            if (error != null)
                setErrorState(error);
            else {
                saveState.setValue(data);
                saveState.setValue(null); // no longer needed
            }
        });
    }

    public LiveData<Pair<ArrayList<String>, ArrayList<String>>> getCodeLoadedState() {
        return code;
    }

    public void loadCode(int taskId) {
        apiTask.executeAsync(new GetProgram(taskId), (data, error) -> {
            if (error != null)
                setErrorState(error);
            else
                code.setValue(data);
        });
    }

    public void clear() {
        state = new MutableLiveData<>(null);
    }
}
