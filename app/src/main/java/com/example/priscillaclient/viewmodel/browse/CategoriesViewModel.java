package com.example.priscillaclient.viewmodel.browse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.GetCategories;
import com.example.priscillaclient.viewmodel.browse.models.Category;
import com.example.priscillaclient.viewmodel.ViewModelBase;

import java.util.ArrayList;

public class CategoriesViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<Category>> state = new MutableLiveData<>(null);

    public LiveData<ArrayList<Category>> getData() {
        return state;
    }

    public void fetchData() {
        if (getData().getValue() == null) {
            apiTask.executeAsync(new GetCategories(), (data, error) -> {
                setError(error);
                state.setValue(data);
            });
        }
    }
}
