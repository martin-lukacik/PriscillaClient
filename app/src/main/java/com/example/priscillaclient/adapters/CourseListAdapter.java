package com.example.priscillaclient.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.priscillaclient.R;
import com.example.priscillaclient.viewmodels.app.models.Course;

import java.util.ArrayList;

public class CourseListAdapter extends ArrayAdapter<Course> {
    private final ArrayList<Course> courses;
    private final boolean isColorblind;

    public CourseListAdapter(Activity context, ArrayList<Course> courses, boolean isColorblind) {
        super(context, R.layout.listview_course, courses);
        this.courses = courses;
        this.isColorblind = isColorblind;
    }

    static class ViewHolder {
        TextView titleText;
        TextView subtitleText;
        TextView contentText;
        TextView taskText;
        TextView programText;

        ProgressBar courseProgress;

        ImageView descriptionIcon;
        ImageView codeIcon;
        ImageView contactIcon;
    }

    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_course, parent, false);

            holder.titleText = view.findViewById(R.id.title);
            holder.subtitleText = view.findViewById(R.id.subtitle);
            holder.contentText = view.findViewById(R.id.content_passed);
            holder.taskText = view.findViewById(R.id.task_passed);
            holder.programText = view.findViewById(R.id.program_passed);
            holder.courseProgress = view.findViewById(R.id.course_progress);
            holder.descriptionIcon = view.findViewById(R.id.ic_description);
            holder.codeIcon = view.findViewById(R.id.ic_code);
            holder.contactIcon = view.findViewById(R.id.ic_contact_support);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String content = courses.get(i).content_passed + " / " + courses.get(i).content_count;
        String task = courses.get(i).task_passed + " / " + courses.get(i).task_count;
        String program = courses.get(i).program_passed + " / " + courses.get(i).program_count;

        holder.titleText.setText(courses.get(i).name);
        holder.subtitleText.setText(courses.get(i).description);
        holder.contentText.setText(content);
        holder.taskText.setText(task);
        holder.programText.setText(program);

        int progress = (int) ((courses.get(i).passed / ((double) courses.get(i).all)) * 100);
        holder.courseProgress.setProgress(progress);

        int color = Color.parseColor(courses.get(i).area_color);

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        if (hsv[2] >= .16)
            hsv[2] -= .16;
        color = Color.HSVToColor(hsv);

        if (isColorblind) {
            TypedValue value = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.colorPrimaryVariant, value, true);
            color = value.data;
        }

        holder.descriptionIcon.setColorFilter(color);
        holder.codeIcon.setColorFilter(color);
        holder.contactIcon.setColorFilter(color);

        Drawable drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.course_title_border, null);
        if (drawable != null)
            drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        holder.titleText.setBackground(drawable);
        holder.titleText.setTextColor(Color.WHITE);

        holder.courseProgress.setProgressTintList(ColorStateList.valueOf(color));

        if (courses.get(i).isPinned) {
            holder.titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_push_pin, 0);
        } else {
            holder.titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        if (courses.get(i).program_count == 0) {
            holder.programText.setVisibility(View.GONE);
            holder.codeIcon.setVisibility(View.GONE);
        } else {
            holder.programText.setVisibility(View.VISIBLE);
            holder.codeIcon.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
