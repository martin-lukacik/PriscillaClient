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
import com.example.priscillaclient.fragments.FragmentAdapter;
import com.example.priscillaclient.fragments.app.ChaptersFragment;
import com.example.priscillaclient.viewmodels.app.CoursesViewModel;
import com.example.priscillaclient.viewmodels.app.models.Course;
import com.example.priscillaclient.viewmodels.browse.AreaCoursesViewModel;
import com.example.priscillaclient.viewmodels.browse.models.AreaCourse;
import com.example.priscillaclient.fragments.FragmentBase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AreaCourseFragment extends FragmentBase implements FragmentAdapter<ArrayList<AreaCourse>> {

    public static final String ARG_AREA_ID = "areaId";

    ArrayList<AreaCourse> areaCourses;
    int areaId = -1;

    public AreaCourseFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_area_course;

        if (getArguments() != null) {
            areaId = getArguments().getInt(ARG_AREA_ID);
        }

        AreaCoursesViewModel viewModel = (AreaCoursesViewModel) getViewModel(AreaCoursesViewModel.class);
        viewModel.getData().observe(this, onResponse(viewModel));
        viewModel.fetchData(areaId);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEmptyView(findViewById(R.id.areaCourseList));
    }

    public void onUpdate(ArrayList<AreaCourse> areaCourses) {

        this.areaCourses = areaCourses;

        ArrayAdapter<AreaCourse> adapter = new ArrayAdapter<AreaCourse>(getActivity(), android.R.layout.simple_list_item_1, areaCourses) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                if (areaCourses.get(position).status == AreaCourse.CourseStatus.OPENED)
                    ((MaterialTextView) view).setTextColor(0xff008000);
                return view;
            }
        };
        ListView areaCourseList = findViewById(R.id.areaCourseList);
        areaCourseList.setAdapter(adapter);
        areaCourseList.setOnItemClickListener(this::courseSelected);
    }

    private void courseSelected(AdapterView<?> adapterView, View view, int i, long l) {
        CoursesViewModel coursesViewModel = (CoursesViewModel) getViewModel(CoursesViewModel.class);
        ArrayList<Course> courses = coursesViewModel.getData().getValue();

        AreaCourse course = areaCourses.get(i);
        if (course.status == AreaCourse.CourseStatus.OPENED) {

            BottomNavigationView navigationView = findViewById(R.id.bottomNavigation);
            navigationView.setSelectedItemId(R.id.menu_dashboard);

            if (courses != null) {
                navigateToCourse(courses, course.id);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(course.title);
            builder.setMessage("Join course " + course.title + " ?");
            builder.setPositiveButton("JOIN", (dialog, id) -> {
                coursesViewModel.joinCourse(course.id);
                coursesViewModel.getJoinState().observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s == null)
                            return;
                        navigate(R.id.menu_dashboard);
                        coursesViewModel.clear();
                        coursesViewModel.fetchData();
                        coursesViewModel.getJoinState().removeObserver(this);
                    }
                });
            });
            builder.setNegativeButton(R.string.cancel, null);

            Dialog d = builder.create();
            d.show();
        }
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