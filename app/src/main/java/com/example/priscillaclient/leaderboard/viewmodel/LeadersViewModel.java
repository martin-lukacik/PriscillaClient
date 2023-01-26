package com.example.priscillaclient.leaderboard.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.leaderboard.api.GetLeaders;
import com.example.priscillaclient.leaderboard.viewmodel.models.LeaderboardItem;
import com.example.priscillaclient.util.ViewModelBase;

import java.util.ArrayList;

public class LeadersViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<LeaderboardItem>> state = new MutableLiveData<>(new ArrayList<>());

    public LiveData<ArrayList<LeaderboardItem>> getData() {
        return state;
    }

    public void fetchData() {
        if (getData().getValue().isEmpty()) {
            apiTask.executeAsync(new GetLeaders(), (data, error) -> {
                setError(error);
                state.setValue(data);
            });
        }
    }
}
