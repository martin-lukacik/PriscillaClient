package com.example.priscillaclient.viewmodels.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.user.GetSettings;
import com.example.priscillaclient.viewmodels.user.models.Settings;
import com.example.priscillaclient.viewmodels.ViewModelBase;

public class SettingsViewModel extends ViewModelBase {
    private final MutableLiveData<Settings> state = new MutableLiveData<>(new Settings());

    public LiveData<Settings> getData() {
        return state;
    }

    public void fetchData() {
        apiTask.executeAsync(new GetSettings(), (data, error) -> {
            if (error != null)
                setErrorState(error);
            else
                state.setValue(data);
        });
    }
}
