package com.example.priscillaclient.fragments.app;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.adapters.CourseListAdapter;
import com.example.priscillaclient.fragments.FragmentAdapter;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.misc.Preferences;
import com.example.priscillaclient.viewmodels.app.CoursesViewModel;
import com.example.priscillaclient.viewmodels.app.models.Course;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CoursesFragment extends FragmentBase implements FragmentAdapter<ArrayList<Course>> {

    // Members
    private CourseListAdapter adapter;
    private ArrayList<Course> courses;

    // View models
    private CoursesViewModel viewModel;

    // Views
    private GridView courseListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_courses;

        viewModel = getViewModel(CoursesViewModel.class);
        viewModel.getData().observe(this, onResponse(viewModel.getError()));
        viewModel.fetchData();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyView(findViewById(R.id.courseListView));

        courseListView = findViewById(R.id.courseListView);
        courseListView.setOnItemClickListener(this::onCourseSelected);
        courseListView.setOnItemLongClickListener(this::onCoursePinned);
    }

    @Override
    public void onUpdate(ArrayList<Course> response) {
        onUpdateCourses(response);

        SharedPreferences settings = requireActivity().getApplicationContext().getSharedPreferences(Preferences.PREFS, 0);
        int pinnedCourseId = settings.getInt(Preferences.PREFS_PINNED_COURSE_ID, -1);

        setPinnedCourse(pinnedCourseId);
    }

    private void setPinnedCourse(int courseId) {
        int courseIndex = -1;

        for (int i = 0; i < courses.size(); ++i) {
            Course course = courses.get(i);
            course.isPinned = false;

            if (courseId == courses.get(i).course_id) {
                course.isPinned = true;
                courseIndex = i;
            }
        }

        if (courseIndex != -1) {
            Course c = courses.remove(courseIndex);
            courses.add(0, c);
        }
    }

    private boolean onCoursePinned(AdapterView<?> adapterView, View view, int i, long l) {
        int courseId = courses.get(i).course_id;

        SharedPreferences settings = requireActivity().getApplicationContext().getSharedPreferences(Preferences.PREFS, 0);
        int savedPinId = settings.getInt(Preferences.PREFS_PINNED_COURSE_ID, -1);

        if (savedPinId == -1) {
            savePinnedCourse(courseId);
            setPinnedCourse(courseId);
            adapter.notifyDataSetChanged();
        } else if (courseId == savedPinId) {
            savePinnedCourse(-1);
            setPinnedCourse(-1);

            onUpdateCourses(viewModel.getData().getValue());
        }

        return true;
    }

    private void onUpdateCourses(ArrayList<Course> courses) {
        if (courses != null) {
            this.courses = new ArrayList<>(courses);
            adapter = new CourseListAdapter(getActivity(), this.courses);
            courseListView.setAdapter(adapter);
        }
    }

    private void savePinnedCourse(int courseId) {
        SharedPreferences settings = requireActivity().getApplicationContext().getSharedPreferences(Preferences.PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(Preferences.PREFS_PINNED_COURSE_ID, courseId);
        editor.apply();
    }

    private void onCourseSelected(AdapterView<?> adapterView, View view, int i, long l){
        Course course = courses.get(i);
        Bundle args = new Bundle();
        args.putInt(ChaptersFragment.ARG_COURSE_ID, course.course_id);
        args.putInt(ChaptersFragment.ARG_COURSE_COLOR, Color.parseColor(course.area_color));

        navigate(R.id.chaptersFragment, args);
    }
}