package com.example.priscillaclient.util;

import androidx.lifecycle.ViewModel;

public abstract class ViewModelBase extends ViewModel {

    protected ApiTask apiTask = new ApiTask();

    protected String error = null;

    public boolean hasError() {
        return error != null;
    }

    public void setError(String message) {
        error = message;
    }

    public String getError() {
        return error;
    }
}
