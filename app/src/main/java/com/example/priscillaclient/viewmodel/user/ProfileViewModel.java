package com.example.priscillaclient.viewmodel.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.user.GetProfile;
import com.example.priscillaclient.viewmodel.ViewModelBase;
import com.example.priscillaclient.viewmodel.user.models.Profile;

import org.json.JSONException;

public class ProfileViewModel extends ViewModelBase {
    private final MutableLiveData<Profile> state = new MutableLiveData<>(new Profile());

    public ProfileViewModel() throws JSONException {
    }

    public LiveData<Profile> getData() {
        return state;
    }

    public void fetchData() {
        apiTask.executeAsync(new GetProfile(), (data, error) -> {
            setError(error);
            state.setValue(data);
        });
    }
}