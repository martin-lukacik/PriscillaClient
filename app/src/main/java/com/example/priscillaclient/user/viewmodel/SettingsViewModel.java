package com.example.priscillaclient.user.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.user.api.GetSettings;
import com.example.priscillaclient.user.viewmodel.models.Settings;
import com.example.priscillaclient.util.ViewModelBase;

public class SettingsViewModel extends ViewModelBase {
    private final MutableLiveData<Settings> state = new MutableLiveData<>(new Settings());

    public LiveData<Settings> getData() {
        return state;
    }

    public void fetchData() {
        apiTask.executeAsync(new GetSettings(), (data, error) -> {
            setError(error);
            state.setValue(data);
        });
    }
}
