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
import com.example.priscillaclient.misc.Preferences;
import com.example.priscillaclient.viewmodels.app.CoursesViewModel;
import com.example.priscillaclient.viewmodels.app.models.Course;
import com.example.priscillaclient.viewmodels.user.models.Theme;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CoursesFragment extends FragmentBase {

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

        // Prepare view models
        viewModel = getViewModel(CoursesViewModel.class);
        viewModel.fetchData(false);
        viewModel.getData().observe(this, this::onUpdate);
        viewModel.getErrorState().observe(this, this::showError);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup views
        courseListView = findViewById(R.id.courseListView);
        courseListView.setOnItemClickListener(this::onCourseSelected);
        courseListView.setOnItemLongClickListener(this::onCoursePinned);
        setEmptyView(courseListView);
    }

    public void onUpdate(ArrayList<Course> courses) {
        if (courses != null) {
            this.courses = new ArrayList<>(courses);
            boolean isColorblind = Theme.THEME_COLORBLIND == preferences.getInt(Preferences.PREFS_THEME_ID, 1);
            adapter = new CourseListAdapter(getActivity(), this.courses, isColorblind);
            courseListView.setAdapter(adapter);

            int savedPinId = preferences.getInt(Preferences.PREFS_PINNED_COURSE_ID, -1);
            setPinnedCourse(savedPinId, false);
        }
    }

    private void onCourseSelected(AdapterView<?> adapterView, View view, int i, long l){
        Course course = courses.get(i);
        Bundle args = new Bundle();
        args.putInt(ChaptersFragment.ARG_COURSE_ID, course.course_id);
        args.putInt(ChaptersFragment.ARG_COURSE_COLOR, Color.parseColor(course.area_color));

        navigate(R.id.chaptersFragment, args);
    }

    private boolean onCoursePinned(AdapterView<?> adapterView, View view, int i, long l) {
        int savedPinId = preferences.getInt(Preferences.PREFS_PINNED_COURSE_ID, -1);
        int courseId = courses.get(i).course_id;

        if (savedPinId == -1) {
            // Nothing pinned
            setPinnedCourse(courseId, true);
            adapter.notifyDataSetChanged();
            courseListView.smoothScrollToPositionFromTop(0, 0);
        } else if (courseId == savedPinId) {
            // Cleared pin, restore original order
            setPinnedCourse(-1, true);
            onUpdate(viewModel.getData().getValue());
        }

        return true;
    }

    private void savePinnedCourse(int courseId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Preferences.PREFS_PINNED_COURSE_ID, courseId);
        editor.apply();
    }

    private void setPinnedCourse(int courseId, boolean save) {
        int courseIndex = -1;

        if (courses != null) {
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

        if (save)
            savePinnedCourse(courseId);
    }
}