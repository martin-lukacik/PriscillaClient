package com.example.priscillaclient.viewmodels.misc;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.misc.GetLeaders;
import com.example.priscillaclient.viewmodels.misc.models.Leader;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;

public class LeadersViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<Leader>> state = new MutableLiveData<>(null);

    public LiveData<ArrayList<Leader>> getData() {
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
