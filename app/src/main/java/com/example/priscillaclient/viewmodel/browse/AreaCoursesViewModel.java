package com.example.priscillaclient.viewmodel.browse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.priscillaclient.api.browse.GetAreaCourses;
import com.example.priscillaclient.viewmodel.browse.models.AreaCourse;
import com.example.priscillaclient.viewmodel.ViewModelBase;

import java.util.ArrayList;

public class AreaCoursesViewModel extends ViewModelBase {
    private final MutableLiveData<ArrayList<AreaCourse>> state = new MutableLiveData<>(null);

    public LiveData<ArrayList<AreaCourse>> getData() {
        return state;
    }

    private int lastAreaId = -1;

    public void fetchData(int areaId) {
        if (lastAreaId != areaId) {
            apiTask.executeAsync(new GetAreaCourses(areaId), (data, error) -> {
                setError(error);
                state.setValue(data);
            });
            lastAreaId = areaId;
        }
    }
}
