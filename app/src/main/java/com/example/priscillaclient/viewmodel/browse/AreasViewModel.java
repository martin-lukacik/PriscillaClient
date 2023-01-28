package com.example.priscillaclient.viewmodel.browse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.GetAreas;
import com.example.priscillaclient.viewmodel.browse.models.Area;
import com.example.priscillaclient.viewmodel.ViewModelBase;

import java.util.ArrayList;

public class AreasViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<Area>> state = new MutableLiveData<>(null);

    public LiveData<ArrayList<Area>> getData() {
        return state;
    }

    private int lastCategoryId = -1;

    public void fetchData(int categoryId) {
        if (lastCategoryId != categoryId) {
            apiTask.executeAsync(new GetAreas(categoryId), (data, error) -> {
                setError(error);
                state.setValue(data);
            });
            lastCategoryId = categoryId;
        }
    }
}