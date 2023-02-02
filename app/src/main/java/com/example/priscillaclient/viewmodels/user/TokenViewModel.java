package com.example.priscillaclient.viewmodels.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.user.GetToken;
import com.example.priscillaclient.viewmodels.ViewModelBase;
import com.example.priscillaclient.viewmodels.user.models.Token;

public class TokenViewModel extends ViewModelBase {
    private final MutableLiveData<Token> state = new MutableLiveData<>(null);

    public LiveData<Token> getData() {
        return state;
    }

    public void fetchData(String username, String password, String email, String grantType) {
        loadingState.setValue(true);
        apiTask.executeAsync(new GetToken(username, password, email, grantType), (data, error) -> {
            loadingState.setValue(false);
            if (error != null)
               setErrorState(error);
            else
                state.setValue(data);
        });
    }
}
