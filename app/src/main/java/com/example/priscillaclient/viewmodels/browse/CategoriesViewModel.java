package com.example.priscillaclient.viewmodels.browse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.GetCategories;
import com.example.priscillaclient.viewmodels.browse.models.Category;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;

public class CategoriesViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<Category>> state = new MutableLiveData<>(null);

    public LiveData<ArrayList<Category>> getData() {
        return state;
    }

    public void fetchData() {
        if (getData().getValue() == null || getData().getValue().isEmpty()) {
            clear();
            setLoadingState(true);
            apiTask.executeAsync(new GetCategories(), (data, error) -> {
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
