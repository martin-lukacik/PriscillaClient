package com.example.priscillaclient.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.priscillaclient.views.adapters.ChapterListAdapter;
import com.example.priscillaclient.R;
import com.example.priscillaclient.api.GetActiveChapters;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.models.Chapter;

import java.util.ArrayList;

public class ChaptersFragment extends FragmentBase {

    private static final String ARG_COURSE_ID = "courseId";

    private int courseId;

    public ChaptersFragment() { }

    public static ChaptersFragment newInstance(int courseId) {
        ChaptersFragment fragment = new ChaptersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (chapters != null)
            onUpdate(chapters);

    }

    ArrayList<Chapter> chapters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (chapters == null)
            new GetActiveChapters(this, courseId).execute();

        return inflater.inflate(R.layout.fragment_chapter, container, false);
    }

    @Override
    public void onUpdate(Object response) {

        if (response == null || getActivity() == null)
            return;

        chapters = Client.getInstance().chapters;

        GridView chaptersListView = findViewById(R.id.chapterListView);

        String[] chaps = new String[chapters.size()];
        for (int i = 0; i < chapters.size(); ++i) {
            Chapter c = chapters.get(i);
            String format = c.name;
            if (c.tasks_finished + c.tasks_nonfinished > 0)
                format += " Tasks: " + c.tasks_finished + " / " + (c.tasks_finished + c.tasks_nonfinished);
            if (c.programs_finished + c.programs_nonfinished > 0)
                format += " Programs: " + c.programs_finished + " / " + (c.programs_finished + c.programs_nonfinished);
            chaps[i] = format;
        }

        ChapterListAdapter adapter = new ChapterListAdapter(getActivity(), chapters);
        chaptersListView.setAdapter(adapter);
        chaptersListView.setOnItemClickListener(this::onItemClick);
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int chapterId = chapters.get(i).id;
        swapFragment(TaskFragment.newInstance(courseId, chapterId));
    }
}