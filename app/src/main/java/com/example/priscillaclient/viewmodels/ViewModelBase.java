package com.example.priscillaclient.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.priscillaclient.api.ApiTask;

public abstract class ViewModelBase extends ViewModel {

    protected ApiTask apiTask = new ApiTask();

    protected String error = null;

    protected final MutableLiveData<Boolean> loadingState = new MutableLiveData<>(false);
    protected final MutableLiveData<String> errorState = new MutableLiveData<>(null);

    public LiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    public LiveData<String> getErrorState() {
        return errorState;
    }

    public boolean hasError() {
        return error != null;
    }

    public void setError(String message) {
        if (message != null && !message.isEmpty())
            error = message;
    }

    public String getError() {
        String e = error;
        error = null;
        return e;
    }
}
