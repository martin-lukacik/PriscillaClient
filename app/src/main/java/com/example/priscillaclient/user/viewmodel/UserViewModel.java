package com.example.priscillaclient.user.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.user.api.DoUpdate;
import com.example.priscillaclient.user.api.GetUser;
import com.example.priscillaclient.user.viewmodel.models.User;
import com.example.priscillaclient.util.ViewModelBase;

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

    public void update(String age, String content_type_id, String country, String group, String lang, String name, String nick, String surname, String theme_id) {
        apiTask.executeAsync(new DoUpdate(age, content_type_id, country, group, lang, name, nick, surname, theme_id), (data, error) -> {
            fetchData();
        });
    }
}
