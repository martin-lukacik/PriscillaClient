package com.example.priscillaclient.fragments.app;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.adapters.CourseListAdapter;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.util.Preferences;
import com.example.priscillaclient.viewmodels.app.CoursesViewModel;
import com.example.priscillaclient.viewmodels.app.models.Course;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CoursesFragment extends FragmentBase {

    ArrayList<Course> courses;
    CourseListAdapter adapter;

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

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridView courseListView = findViewById(R.id.courseListView);

        View emptyView = View.inflate(getContext(), R.layout.loading_view, null);
        requireActivity().addContentView(emptyView, courseListView.getLayoutParams());
        courseListView.setEmptyView(emptyView);
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

        SharedPreferences settings = requireActivity().getApplicationContext().getSharedPreferences(Preferences.PREFS, 0);
        int savedPinId = settings.getInt(Preferences.PREFS_PINNED_COURSE_ID, -1);

        Course course = courses.get(i);
        if (savedPinId == -1) {
            togglePin(course.course_id);
            adapter.notifyDataSetChanged();
        } else if (course.course_id == savedPinId) {
            togglePin(-1);
            CoursesViewModel viewModel = (CoursesViewModel) getViewModel(CoursesViewModel.class);

            if (viewModel.getData().getValue() != null) {
                courses = new ArrayList<>(viewModel.getData().getValue());
                adapter = new CourseListAdapter(getActivity(), courses);
                GridView courseListView = requireActivity().findViewById(R.id.courseListView);
                courseListView.setAdapter(adapter);
            }
        }

        return true;
    }

    private void togglePin(int courseId) {
        SharedPreferences settings = requireActivity().getApplicationContext().getSharedPreferences(Preferences.PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(Preferences.PREFS_PINNED_COURSE_ID, courseId);
        editor.apply();
        pinCourse(courseId);
    }

    private void courseSelected(AdapterView<?> adapterView, View view, int i, long l){
        int courseId = courses.get(i).course_id;
        int courseColor = Color.parseColor(courses.get(i).area_color);
        Bundle args = new Bundle();
        args.putInt(ChaptersFragment.ARG_COURSE_ID, courseId);
        args.putInt(ChaptersFragment.ARG_COURSE_COLOR, courseColor);
        navigate(R.id.chaptersFragment, args);
    }

    public void onUpdate(ArrayList<Course> response) {
        courses = new ArrayList<>(response);

        SharedPreferences settings = requireActivity().getApplicationContext().getSharedPreferences(Preferences.PREFS, 0);
        int pinnedCourseId = settings.getInt(Preferences.PREFS_PINNED_COURSE_ID, -1);
        pinCourse(pinnedCourseId);

        adapter = new CourseListAdapter(getActivity(), courses);

        GridView courseListView = requireActivity().findViewById(R.id.courseListView);
        courseListView.setAdapter(adapter);
        courseListView.setOnItemClickListener(this::courseSelected);
        courseListView.setOnItemLongClickListener(this::coursePinned);
    }
}