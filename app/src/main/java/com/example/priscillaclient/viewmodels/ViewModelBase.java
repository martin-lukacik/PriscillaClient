package com.example.priscillaclient.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.priscillaclient.api.ApiTask;

public abstract class ViewModelBase extends ViewModel {

    protected ApiTask apiTask = new ApiTask();

    protected String error = null;

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
