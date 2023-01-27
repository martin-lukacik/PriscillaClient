package com.example.priscillaclient.browse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.app.viewmodel.CoursesViewModel;
import com.example.priscillaclient.app.viewmodel.models.Course;
import com.example.priscillaclient.browse.viewmodel.AreaCoursesViewModel;
import com.example.priscillaclient.browse.viewmodel.models.AreaCourse;
import com.example.priscillaclient.util.FragmentBase;
import com.example.priscillaclient.util.LoadingDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class AreaCourseFragment extends FragmentBase {

    ArrayList<AreaCourse> areaCourses;
    int areaId = -1;

    public AreaCourseFragment() { }

    boolean firstLoad = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_area_course;

        if (getArguments() != null) {
            areaId = getArguments().getInt("areaId");
        }

        dialog = new LoadingDialog(getActivity());

        AreaCoursesViewModel viewModel = (AreaCoursesViewModel) getViewModel(AreaCoursesViewModel.class);
        viewModel.getData().observe(this, (data) -> {
            if (viewModel.hasError())
                showError(viewModel.getError());
            else if (data != null && !firstLoad) {
                onUpdate(data);
                dialog.dismiss();
            }
            firstLoad = false;
        });
        viewModel.fetchData(areaId);
        dialog.show();
    }

    public void onUpdate(ArrayList<AreaCourse> areaCourses) {

        this.areaCourses = areaCourses;

        ArrayAdapter<AreaCourse> adapter = new ArrayAdapter<AreaCourse>(getActivity(), android.R.layout.simple_list_item_1, areaCourses) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                if (areaCourses.get(position).status == AreaCourse.CourseStatus.OPENED)
                    ((MaterialTextView) view).setTextColor(Color.parseColor("#008000"));
                return view;
            }
        };
        ListView areaCourseList = findViewById(R.id.areaCourseList);
        areaCourseList.setAdapter(adapter);
        areaCourseList.setOnItemClickListener(this::courseSelected);
    }

    private void courseSelected(AdapterView<?> adapterView, View view, int i, long l) {
        AreaCourse course = areaCourses.get(i);
        if (course.status == AreaCourse.CourseStatus.OPENED) {

            BottomNavigationView navigationView = findViewById(R.id.bottomNavigation);
            navigationView.setSelectedItemId(R.id.menu_dashboard);

            CoursesViewModel coursesViewModel = (CoursesViewModel) getViewModel(CoursesViewModel.class);
            ArrayList<Course> courses = coursesViewModel.getData().getValue();

            int courseColor = 0;
            for (Course c : courses) {
                if (c.course_id == course.id) {
                    courseColor = Color.parseColor(c.area_color);
                    break;
                }
            }

            Bundle args = new Bundle();
            args.putInt("courseId", course.id);
            args.putInt("courseColor", courseColor);

            navigate(R.id.coursesFragment); // needed for back stack
            navigate(R.id.chaptersFragment, args);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(course.title);
            builder.setMessage("Join course " + course.title + " ?");
            builder.setPositiveButton("JOIN", (dialog, id) -> {
                // TODO join course
            });
            builder.setNegativeButton("Cancel", (dialog, id) -> {
            });

            Dialog d = builder.create();
            d.show();
        }
    }
}