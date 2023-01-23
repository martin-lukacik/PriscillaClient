package com.example.priscillaclient.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.MainActivity;
import com.example.priscillaclient.R;
import com.example.priscillaclient.api.browse.GetAreaCourses;
import com.example.priscillaclient.models.Area;
import com.example.priscillaclient.models.AreaCourse;
import com.example.priscillaclient.models.Client;

import java.util.ArrayList;

public class AreaCourseFragment extends FragmentBase {

    int areaId = -1;

    public AreaCourseFragment() { }

    public AreaCourseFragment(int areaId) {
        this.areaId = areaId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new GetAreaCourses(this, areaId).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_area_course, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<AreaCourse> areaCourses = Client.getInstance().areaCourses;
        if (!areaCourses.isEmpty())
            onUpdate(areaCourses);
    }

    @Override
    public void onUpdate(Object response) {
        ArrayList<AreaCourse> areaCourses = Client.getInstance().areaCourses;

        ArrayAdapter<AreaCourse> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, areaCourses);
        ListView areaCourseList = findViewById(R.id.areaCourseList);
        areaCourseList.setAdapter(adapter);
        areaCourseList.setOnItemClickListener(this::courseSelected);
    }

    private void courseSelected(AdapterView<?> adapterView, View view, int i, long l) {
        AreaCourse course = Client.getInstance().areaCourses.get(i);
        if (course.status == AreaCourse.CourseStatus.OPENED) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("course_id", course.id);
            startActivity(intent);
        }

        // TODO join course
    }
}