package com.example.priscillaclient.fragments.browse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.lifecycle.Observer;

import com.example.priscillaclient.R;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.fragments.app.ChaptersFragment;
import com.example.priscillaclient.viewmodels.app.CoursesViewModel;
import com.example.priscillaclient.viewmodels.app.models.Course;
import com.example.priscillaclient.viewmodels.browse.AreaCoursesViewModel;
import com.example.priscillaclient.viewmodels.browse.models.AreaCourse;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AreaCourseFragment extends FragmentBase {

    // Arguments
    public static final String ARG_AREA_ID = "areaId";

    // Members
    private int areaId = -1;
    private ArrayList<Course> courses;
    private ArrayList<AreaCourse> areaCourses;

    // Views
    ListView areaCourseList;

    // View models
    private AreaCoursesViewModel viewModel;
    private CoursesViewModel coursesViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_area_course;

        if (getArguments() != null) {
            areaId = getArguments().getInt(ARG_AREA_ID);
        }

        viewModel = getViewModel(AreaCoursesViewModel.class);
        viewModel.fetchData(areaId);

        coursesViewModel = getViewModel(CoursesViewModel.class);
        courses = coursesViewModel.getData().getValue();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup views
        areaCourseList = findViewById(R.id.areaCourseList);
        areaCourseList.setOnItemClickListener(this::onCourseSelected);
        setEmptyView(areaCourseList);

        // Setup observers
        viewModel.getData().observe(getViewLifecycleOwner(), this::onUpdate);
        viewModel.getErrorState().observe(getViewLifecycleOwner(), this::showError);
    }

    public void onUpdate(ArrayList<AreaCourse> areaCourses) {
        if (areaCourses != null) {
            this.areaCourses = areaCourses;

            ArrayAdapter<AreaCourse> adapter = new ArrayAdapter<AreaCourse>(getActivity(), android.R.layout.simple_list_item_1, areaCourses) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    if (areaCourses.get(position).status != AreaCourse.CourseStatus.OPENED) {
                        String str = ((MaterialTextView) view).getText().toString();
                        char unopenedCourseTag = '✱';
                        ((MaterialTextView) view).setText(unopenedCourseTag + " " + str);
                    }
                    return view;
                }
            };

            areaCourseList.setAdapter(adapter);
        }
    }

    private void onCourseSelected(AdapterView<?> adapterView, View view, int i, long l) {
        AreaCourse course = areaCourses.get(i);

        if (course.status == AreaCourse.CourseStatus.OPENED) {
            if (courses != null) {
                navigateToCourse(courses, course.id);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(course.title);
            builder.setMessage(getString(R.string.join_course) + " " + course.title + " ?");
            builder.setPositiveButton(getString(R.string.join), (dialog, id) -> joinCourse(course.id));
            builder.setNegativeButton(R.string.cancel, null);

            Dialog d = builder.create();
            d.show();
        }
    }

    private void joinCourse(int courseId) {
        coursesViewModel.joinCourse(courseId);
        coursesViewModel.getJoinState().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null)
                    return;
                navigate(R.id.menu_dashboard);
                coursesViewModel.fetchData(true);
                coursesViewModel.getJoinState().removeObserver(this); // observer no longer needed
            }
        });
    }

    private void navigateToCourse(ArrayList<Course> courses, int courseId) {
        int courseColor = 0;
        for (Course c : courses) {
            if (c.course_id == courseId) {
                courseColor = Color.parseColor(c.area_color);
                break;
            }
        }

        Bundle args = new Bundle();
        args.putInt(ChaptersFragment.ARG_COURSE_ID, courseId);
        args.putInt(ChaptersFragment.ARG_COURSE_COLOR, courseColor);

        navigate(R.id.menu_dashboard); // needed for back stack
        navigate(R.id.chaptersFragment, args);
    }
}