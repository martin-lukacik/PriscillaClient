package com.example.priscillaclient;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.priscillaclient.models.Course;

import java.util.ArrayList;

public class CourseListAdapter extends ArrayAdapter<Course> {
    private final Activity context;
    private final ArrayList<Course> courses;

    public CourseListAdapter(Activity context, ArrayList<Course> courses) {
        super(context, R.layout.listview_course, courses);
        this.context = context;
        this.courses = courses;
    }

    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_course, null,true);

        TextView titleText = rowView.findViewById(R.id.title);
        TextView subtitleText = rowView.findViewById(R.id.subtitle);
        TextView passedText = rowView.findViewById(R.id.passed);

        titleText.setText(courses.get(i).name);
        subtitleText.setText(courses.get(i).description);
        passedText.setText(courses.get(i).getUserData("passed") + " / " + courses.get(i).getUserData("all"));

        int color = Color.parseColor(courses.get(i).area_color);
        Color c = Color.valueOf(color);

        rowView.setBackgroundColor(color);
        titleText.setTextColor(Color.WHITE);

        return rowView;
    }
}
