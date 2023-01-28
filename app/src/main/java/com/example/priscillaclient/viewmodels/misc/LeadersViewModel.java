package com.example.priscillaclient.viewmodels.misc;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.misc.GetLeaders;
import com.example.priscillaclient.viewmodels.misc.models.LeaderboardItem;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;

public class LeadersViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<LeaderboardItem>> state = new MutableLiveData<>(new ArrayList<>());

    public LiveData<ArrayList<LeaderboardItem>> getData() {
        return state;
    }

    public void fetchData() {
        if (getData().getValue() == null || getData().getValue().isEmpty()) {
            apiTask.executeAsync(new GetLeaders(), (data, error) -> {
                setError(error);
                state.setValue(data);
            });
        }
    }
}
