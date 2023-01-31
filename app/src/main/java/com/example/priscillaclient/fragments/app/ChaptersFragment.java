package com.example.priscillaclient.fragments.app;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.adapters.ChapterListAdapter;
import com.example.priscillaclient.fragments.FragmentAdapter;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.viewmodels.app.ChaptersViewModel;
import com.example.priscillaclient.viewmodels.app.models.Chapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChaptersFragment extends FragmentBase implements FragmentAdapter<ArrayList<Chapter>> {

    // Arguments
    public static final String ARG_COURSE_ID = "courseId";
    public static final String ARG_COURSE_COLOR = "courseColor";

    // Members
    private int courseId;
    private int courseColor;
    private ArrayList<Chapter> chapters = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_chapter;

        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
            courseColor = getArguments().getInt(ARG_COURSE_COLOR);
        }

        ChaptersViewModel viewModel = getViewModel(ChaptersViewModel.class);
        viewModel.getData().observe(this, onResponse(viewModel.getError()));
        viewModel.fetchData(courseId);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyView(findViewById(R.id.chapterListView));
    }

    @Override
    public void onUpdate(ArrayList<Chapter> chapters) {
        this.chapters = (ArrayList<Chapter>) chapters;

        ChapterListAdapter adapter = new ChapterListAdapter(getActivity(), this.chapters, courseColor);
        GridView chaptersListView = findViewById(R.id.chapterListView);
        chaptersListView.setAdapter(adapter);
        chaptersListView.setOnItemClickListener(this::onChapterSelected);
    }

    private void onChapterSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle args = new Bundle();
        args.putInt(TaskFragment.ARG_COURSE_ID, courseId);
        args.putInt(TaskFragment.ARG_CHAPTER_ID, chapters.get(i).id);

        navigate(R.id.taskFragment, args);
    }
}