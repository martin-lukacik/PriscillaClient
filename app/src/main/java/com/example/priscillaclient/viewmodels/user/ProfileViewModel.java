package com.example.priscillaclient.viewmodels.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.user.GetProfile;
import com.example.priscillaclient.viewmodels.ViewModelBase;
import com.example.priscillaclient.viewmodels.user.models.Profile;

public class ProfileViewModel extends ViewModelBase {
    private final MutableLiveData<Profile> state = new MutableLiveData<>(null);

    public ProfileViewModel() { }

    public LiveData<Profile> getData() {
        return state;
    }

    public void fetchData() {
        state.setValue(null);
        apiTask.executeAsync(new GetProfile(), (data, error) -> {
            if (error != null)
                setErrorState(error);
            else
                state.setValue(data);
        });
    }
}
