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

import com.example.priscillaclient.R;
import com.example.priscillaclient.viewmodels.app.CoursesViewModel;
import com.example.priscillaclient.viewmodels.app.models.Course;
import com.example.priscillaclient.viewmodels.browse.AreaCoursesViewModel;
import com.example.priscillaclient.viewmodels.browse.models.AreaCourse;
import com.example.priscillaclient.fragments.FragmentBase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AreaCourseFragment extends FragmentBase {

    ArrayList<AreaCourse> areaCourses;
    int areaId = -1;

    public AreaCourseFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_area_course;

        if (getArguments() != null) {
            areaId = getArguments().getInt("areaId");
        }

        AreaCoursesViewModel viewModel = (AreaCoursesViewModel) getViewModel(AreaCoursesViewModel.class);
        viewModel.getData().observe(this, (data) -> {
            if (viewModel.hasError())
                showError(viewModel.getError());
            else if (data != null)
                onUpdate(data);
        });
        viewModel.fetchData(areaId);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView areaCourseList = findViewById(R.id.areaCourseList);

        View emptyView = View.inflate(getContext(), R.layout.loading_view, null);
        requireActivity().addContentView(emptyView, areaCourseList.getLayoutParams());
        areaCourseList.setEmptyView(emptyView);
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

            if (courses != null) {
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
            }
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