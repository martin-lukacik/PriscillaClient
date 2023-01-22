package com.example.priscillaclient.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.app.GetChapters;
import com.example.priscillaclient.models.Chapter;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.views.adapters.ChapterListAdapter;

import org.jetbrains.annotations.NotNull;

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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (chapters == null)
            new GetChapters(this, courseId).execute();

        return inflater.inflate(R.layout.fragment_chapter, container, false);
    }

    @Override
    public void onUpdate(Object response) {

        if (response == null || getActivity() == null)
            return;

        chapters = Client.getInstance().chapters;

        GridView chaptersListView = findViewById(R.id.chapterListView);
        ChapterListAdapter adapter = new ChapterListAdapter(getActivity(), chapters);
        chaptersListView.setAdapter(adapter);
        chaptersListView.setOnItemClickListener(this::onItemClick);
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int chapterId = chapters.get(i).id;
        swapFragment(TaskFragment.newInstance(chapterId));
    }
}