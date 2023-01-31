package com.example.priscillaclient.viewmodels.misc;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.misc.GetLeaders;
import com.example.priscillaclient.viewmodels.misc.models.Leader;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;

public class LeadersViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<Leader>> state = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>(false);

    public LiveData<ArrayList<Leader>> getData() {
        return state;
    }
    public LiveData<Boolean> loadingState() {
        return loadingState;
    }

    public void fetchData(boolean forceFetch) {
        if (forceFetch || getData().getValue() == null || getData().getValue().isEmpty()) {
            loadingState.setValue(true);
            apiTask.executeAsync(new GetLeaders(), (data, error) -> {
                loadingState.setValue(false);
                setError(error);
                state.setValue(data);
            });
        }
    }
}
