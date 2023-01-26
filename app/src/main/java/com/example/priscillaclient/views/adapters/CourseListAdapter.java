package com.example.priscillaclient.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.priscillaclient.R;
import com.example.priscillaclient.viewmodel.app.models.Course;

import java.util.ArrayList;

public class CourseListAdapter extends ArrayAdapter<Course> {
    private final Activity context;
    private final ArrayList<Course> courses;

    public CourseListAdapter(Activity context, ArrayList<Course> courses) {
        super(context, R.layout.listview_course, courses);
        this.context = context;
        this.courses = courses;
    }

    static class ViewHolder{
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        holder.titleText.setText(courses.get(i).name);
        holder.subtitleText.setText(courses.get(i).description);
        holder.contentText.setText(courses.get(i).content_passed + " / " + courses.get(i).content_count);
        holder.taskText.setText(courses.get(i).task_passed + " / " + courses.get(i).task_count);
        holder.programText.setText(courses.get(i).program_passed + " / " + courses.get(i).program_count);

        int progress = (int) ((courses.get(i).passed / ((double) courses.get(i).all)) * 100);
        holder.courseProgress.setProgress(progress);

        int color = Color.parseColor(courses.get(i).area_color);

        holder.descriptionIcon.setColorFilter(color);
        holder.codeIcon.setColorFilter(color);
        holder.contactIcon.setColorFilter(color);

        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.course_title_border, null);//context.getResources().getDrawable(R.drawable.course_title_border);
        if (drawable != null)
            drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        holder.titleText.setBackground(drawable);

        Drawable progressDrawable = holder.courseProgress.getProgressDrawable();
        if (progressDrawable != null)
            progressDrawable.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        holder.courseProgress.setProgressDrawable(progressDrawable);
        holder.titleText.setTextColor(Color.WHITE);

        if (courses.get(i).isPinned) {
            holder.titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_push_pin, 0);
        } else {
            holder.titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        return view;
    }
}
