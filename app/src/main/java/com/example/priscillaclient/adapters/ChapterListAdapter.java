package com.example.priscillaclient.adapters;

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
import com.example.priscillaclient.viewmodel.app.models.Chapter;

import java.util.ArrayList;

public class ChapterListAdapter extends ArrayAdapter<Chapter> {
    private final Activity context;
    private final ArrayList<Chapter> chapters;

    private final int color;

    public ChapterListAdapter(Activity context, ArrayList<Chapter> chapters, int color) {
        super(context, R.layout.listview_chapter, chapters);
        this.context = context;
        this.chapters = chapters;
        this.color = color;
    }

    static class ViewHolder{
        TextView chapterTitle;

        ProgressBar codeProgress;
        ProgressBar taskProgress;

        ImageView codeIcon;
        ImageView contactIcon;
    }

    public View getView(int i, View view, ViewGroup parent) {

        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_chapter, parent, false);

            holder.chapterTitle = view.findViewById(R.id.chapterTitle);
            holder.taskProgress = view.findViewById(R.id.task_progress);
            holder.codeProgress = view.findViewById(R.id.code_progress);
            holder.codeIcon = view.findViewById(R.id.chapter_ic_code);
            holder.contactIcon = view.findViewById(R.id.chapter_ic_contact_support);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.chapterTitle.setText(chapters.get(i).name);

        int progress = (int) ((chapters.get(i).tasks_finished / ((double) chapters.get(i).tasks_finished + chapters.get(i).tasks_nonfinished)) * 100);
        holder.taskProgress.setProgress(progress);

        progress = (int) ((chapters.get(i).programs_finished / ((double) chapters.get(i).programs_finished + chapters.get(i).programs_nonfinished)) * 100);
        holder.codeProgress.setProgress(progress);

        holder.codeIcon.setColorFilter(color);
        holder.contactIcon.setColorFilter(color);

        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.course_title_border, null);//context.getResources().getDrawable(R.drawable.course_title_border);
        if (drawable != null)
            drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        holder.chapterTitle.setBackground(drawable);
        holder.chapterTitle.setTextColor(Color.WHITE);

        Drawable progressDrawable = holder.taskProgress.getProgressDrawable();
        if (progressDrawable != null)
            progressDrawable.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        holder.taskProgress.setProgressDrawable(progressDrawable);

        progressDrawable = holder.codeProgress.getProgressDrawable();
        if (progressDrawable != null)
            progressDrawable.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        holder.codeProgress.setProgressDrawable(progressDrawable);

        return view;
    }
}
