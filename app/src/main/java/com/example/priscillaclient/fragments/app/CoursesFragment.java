package com.example.priscillaclient.fragments.app;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.lifecycle.ViewModelProviders;

import com.example.priscillaclient.R;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.viewmodels.app.models.Course;
import com.example.priscillaclient.viewmodels.app.CoursesViewModel;
import com.example.priscillaclient.adapters.CourseListAdapter;

import java.util.ArrayList;

public class CoursesFragment extends FragmentBase {

    ArrayList<Course> courses;
    CourseListAdapter adapter;

    final String PREF_SET = "settings";

    public CoursesFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_courses;

        CoursesViewModel viewModel = (CoursesViewModel) getViewModel(CoursesViewModel.class);
        viewModel.getData().observe(this, (data) -> {
            if (viewModel.hasError())
                showError(viewModel.getError());
            else
                onUpdate(data);
        });
        viewModel.fetchData();
    }

    void pinCourse(int courseId) {
        for (int i = 0; i < courses.size(); ++i) {
            courses.get(i).isPinned = false;

            if (courseId == courses.get(i).course_id) {
                courses.get(i).isPinned = true;
                Course c = courses.remove(i);
                courses.add(0, c);
            }
        }
    }

    private boolean coursePinned(AdapterView<?> adapterView, View view, int i, long l) {

        if (getActivity() == null)
            return true;

        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREF_SET, 0);
        int pinnedCourseId = settings.getInt("pinnedCourseId", -1);

        Course course = courses.get(i);
        if (pinnedCourseId == -1) {
            togglePin(course.course_id);
            adapter.notifyDataSetChanged();
        } else if (course.course_id == pinnedCourseId) {
            togglePin(-1);
            CoursesViewModel viewModel = ViewModelProviders.of(this).get(CoursesViewModel.class);

            if (viewModel.getData().getValue() != null) {
                courses = new ArrayList<>(viewModel.getData().getValue());
                adapter = new CourseListAdapter(getActivity(), courses);
                GridView courseListView = getActivity().findViewById(R.id.courseListView);
                courseListView.setAdapter(adapter);
            }
        }

        return true;
    }

    private void togglePin(int courseId) {

        if (getActivity() == null)
            return;

        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREF_SET, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("pinnedCourseId", courseId);
        editor.apply();
        pinCourse(courseId);
    }

    private void courseSelected(AdapterView<?> adapterView, View view, int i, long l){
        int courseId = courses.get(i).course_id;
        int courseColor = Color.parseColor(courses.get(i).area_color);
        Bundle args = new Bundle();
        args.putInt("courseId", courseId);
        args.putInt("courseColor", courseColor);
        navigate(R.id.chaptersFragment, args);
    }

    public void onUpdate(ArrayList<Course> response) {

        if (getActivity() == null)
            return;

        courses = new ArrayList<>(response);

        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREF_SET, 0);
        int pinnedCourseId = settings.getInt("pinnedCourseId", -1);
        pinCourse(pinnedCourseId);

        adapter = new CourseListAdapter(getActivity(), courses);

        GridView courseListView = getActivity().findViewById(R.id.courseListView);
        courseListView.setAdapter(adapter);
        courseListView.setOnItemClickListener(this::courseSelected);
        courseListView.setOnItemLongClickListener(this::coursePinned);
    }
}