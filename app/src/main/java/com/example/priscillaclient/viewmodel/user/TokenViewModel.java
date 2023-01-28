package com.example.priscillaclient.viewmodel.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.user.GetToken;
import com.example.priscillaclient.viewmodel.ViewModelBase;
import com.example.priscillaclient.viewmodel.user.models.Token;

public class TokenViewModel extends ViewModelBase {
    private final MutableLiveData<Token> state = new MutableLiveData<>(null);

    public LiveData<Token> getData() {
        return state;
    }

    public void fetchData(String username, String password, String email, String grantType) {
        apiTask.executeAsync(new GetToken(username, password, email, grantType), (data, error) -> {
            setError(error);
            state.setValue(data);
        });
    }
}
