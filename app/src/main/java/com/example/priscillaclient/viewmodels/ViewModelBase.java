package com.example.priscillaclient.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.priscillaclient.api.ApiTask;

public abstract class ViewModelBase extends ViewModel {

    protected final ApiTask apiTask = new ApiTask();

    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorState = new MutableLiveData<>(null);

    public LiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    public LiveData<String> getErrorState() {
        return errorState;
    }

    protected void setErrorState(String error) {
        errorState.setValue(error);
        errorState.setValue(null);
    }

    protected void setLoadingState(boolean value) {
        loadingState.setValue(value);
    }
}
