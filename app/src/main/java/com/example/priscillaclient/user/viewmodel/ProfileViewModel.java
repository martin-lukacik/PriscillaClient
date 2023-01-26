package com.example.priscillaclient.user.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.user.api.GetProfile;
import com.example.priscillaclient.util.ViewModelBase;
import com.example.priscillaclient.user.viewmodel.models.Profile;

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
