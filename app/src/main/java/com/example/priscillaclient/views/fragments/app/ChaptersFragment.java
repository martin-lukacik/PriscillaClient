package com.example.priscillaclient.views.fragments.app;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.app.GetChapters;
import com.example.priscillaclient.models.Chapter;
import com.example.priscillaclient.views.adapters.ChapterListAdapter;
import com.example.priscillaclient.views.fragments.FragmentBase;

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
        layoutId = R.layout.fragment_chapter;

        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
        }

        new GetChapters(this, courseId).execute();
    }

    @Override
    public void onUpdate(Object response) {

        ArrayList<Chapter> chapters = client.chapters;

        GridView chaptersListView = findViewById(R.id.chapterListView);
        ChapterListAdapter adapter = new ChapterListAdapter(getActivity(), chapters);
        chaptersListView.setAdapter(adapter);
        chaptersListView.setOnItemClickListener(this::onItemClick);
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int chapterId = client.chapters.get(i).id;
        navigate(TaskFragment.newInstance(chapterId));
    }
}