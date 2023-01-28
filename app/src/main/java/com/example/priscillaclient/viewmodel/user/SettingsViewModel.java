package com.example.priscillaclient.viewmodel.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.user.GetSettings;
import com.example.priscillaclient.viewmodel.user.models.Settings;
import com.example.priscillaclient.viewmodel.ViewModelBase;

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
