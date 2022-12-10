package com.example.priscillaclient;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
        TextView contentText = rowView.findViewById(R.id.content_passed);
        TextView taskText = rowView.findViewById(R.id.task_passed);
        TextView programText = rowView.findViewById(R.id.program_passed);
        ProgressBar courseProgress = rowView.findViewById(R.id.course_progress);

        ImageView descriptionIcon = rowView.findViewById(R.id.ic_description);
        ImageView codeIcon = rowView.findViewById(R.id.ic_code);
        ImageView contactIcon = rowView.findViewById(R.id.ic_contact_support);

        titleText.setText(courses.get(i).name);
        subtitleText.setText(courses.get(i).description);
        contentText.setText(courses.get(i).getUserData("content_passed") + " / " + courses.get(i).getUserData("content_count"));
        taskText.setText(courses.get(i).getUserData("task_passed") + " / " + courses.get(i).getUserData("task_count"));
        programText.setText(courses.get(i).getUserData("program_passed") + " / " + courses.get(i).getUserData("program_count"));

        int progress = (int) ((courses.get(i).getUserData("passed") / ((double) courses.get(i).getUserData("all"))) * 100);
        courseProgress.setProgress(progress);

        int color = Color.parseColor(courses.get(i).area_color);

        descriptionIcon.setColorFilter(color);
        codeIcon.setColorFilter(color);
        contactIcon.setColorFilter(color);

        Drawable drawable = context.getResources().getDrawable(R.drawable.course_title_border);
        drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        titleText.setBackground(drawable);

        Drawable progressDrawable = courseProgress.getProgressDrawable();
        progressDrawable.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        courseProgress.setProgressDrawable(progressDrawable);
        //titleText.setBackgroundColor(color);
        titleText.setTextColor(Color.WHITE);

        if (courses.get(i).isPinned) {
            titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_push_pin, 0);
        }

        return rowView;
    }
}
