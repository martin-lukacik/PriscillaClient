package com.example.priscillaclient.viewmodels.browse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.GetAreas;
import com.example.priscillaclient.viewmodels.browse.models.Area;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;

public class AreasViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<Area>> state = new MutableLiveData<>(null);

    public LiveData<ArrayList<Area>> getData() {
        return state;
    }

    private int lastCategoryId = -1;

    public void fetchData(int categoryId) {
        if (lastCategoryId != categoryId) {
            lastCategoryId = categoryId;

            clear();
            setLoadingState(true);
            apiTask.executeAsync(new GetAreas(categoryId), (data, error) -> {
                setLoadingState(false);
                if (error != null)
                    setErrorState(error);
                else
                    state.setValue(data);
            });
        }
    }

    public void clear() {
        state.setValue(null);
    }
}
