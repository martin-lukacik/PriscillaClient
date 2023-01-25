package com.example.priscillaclient.views.fragments.browse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.browse.GetAreaCourses;
import com.example.priscillaclient.models.AreaCourse;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.views.fragments.FragmentBase;
import com.example.priscillaclient.views.fragments.app.ChaptersFragment;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AreaCourseFragment extends FragmentBase implements HttpResponse<Object> {

    int areaId = -1;

    public AreaCourseFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_area_course;

        if (getArguments() != null) {
            areaId = getArguments().getInt("areaId");
        }

        new GetAreaCourses(this, areaId).execute();
    }

    @Override
    public void onUpdate(Object response) {
        ArrayList<AreaCourse> areaCourses = Client.getInstance().areaCourses;
        ArrayAdapter<AreaCourse> adapter = new ArrayAdapter<AreaCourse>(getActivity(), android.R.layout.simple_list_item_1, areaCourses) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                if (client.areaCourses.get(position).status == AreaCourse.CourseStatus.OPENED)
                    ((MaterialTextView) view).setTextColor(Color.parseColor("#00FF00"));
                return view;
            }
        };
        ListView areaCourseList = findViewById(R.id.areaCourseList);
        areaCourseList.setAdapter(adapter);
        areaCourseList.setOnItemClickListener(this::courseSelected);
    }

    private void courseSelected(AdapterView<?> adapterView, View view, int i, long l) {
        AreaCourse course = Client.getInstance().areaCourses.get(i);
        if (course.status == AreaCourse.CourseStatus.OPENED) {
            Bundle args = new Bundle();
            args.putInt("courseId", course.id);
            navigate(R.id.chaptersFragment, args);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(course.title);
        builder.setMessage("Join course " + course.title + " ?");
        builder.setPositiveButton("JOIN", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // TODO join course
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        Dialog d = builder.create();
        d.show();
    }
}