package com.example.priscillaclient.viewmodels.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.user.DoUpdate;
import com.example.priscillaclient.api.user.GetUser;
import com.example.priscillaclient.viewmodels.user.models.User;
import com.example.priscillaclient.viewmodels.ViewModelBase;

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

    public void update(int age, int content_type_id, int country, String group, int lang, String name, String nick, String surname, int theme_id) {
        state.setValue(null);
        apiTask.executeAsync(
            new DoUpdate(age, content_type_id, country, group, lang, name, nick, surname, theme_id),
            (data, error) -> {
                setError(error);
                fetchData();
            }
        );
    }
}
