package com.example.priscillaclient.viewmodel.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.user.GetUser;
import com.example.priscillaclient.models.User;
import com.example.priscillaclient.viewmodel.ViewModelBase;

public class UserViewModel extends ViewModelBase {
    private final MutableLiveData<User> state = new MutableLiveData<>(null);

    public LiveData<User> getData() {
        return state;
    }

    public void fetchData() {
        apiTask.executeAsync(new GetUser(), (data, error) -> {
            setError(error);
            state.setValue(data);
        });
    }
}
