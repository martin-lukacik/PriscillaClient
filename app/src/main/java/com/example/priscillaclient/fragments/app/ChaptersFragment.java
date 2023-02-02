package com.example.priscillaclient.fragments.app;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.adapters.ChapterListAdapter;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.viewmodels.app.ChaptersViewModel;
import com.example.priscillaclient.viewmodels.app.models.Chapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChaptersFragment extends FragmentBase {

    // Arguments
    public static final String ARG_COURSE_ID = "courseId";
    public static final String ARG_COURSE_COLOR = "courseColor";

    // Members
    private int courseId;
    private int courseColor;
    private ArrayList<Chapter> chapters = new ArrayList<>();

    // Views
    GridView chaptersListView;

    // View models
    ChaptersViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_chapter;

        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
            courseColor = getArguments().getInt(ARG_COURSE_COLOR);
        }

        // Prepare view models
        viewModel = getViewModel(ChaptersViewModel.class);
        viewModel.fetchData(courseId);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup views
        chaptersListView = findViewById(R.id.chapterListView);
        chaptersListView.setOnItemClickListener((a, v, index, l) -> onChapterSelected(index));
        setEmptyView(chaptersListView);

        // Setup observers
        viewModel.getData().observe(getViewLifecycleOwner(), this::onUpdate);
        viewModel.getErrorState().observe(getViewLifecycleOwner(), this::showError);
    }

    public void onUpdate(ArrayList<Chapter> chapters) {
        if (chapters != null) {
            this.chapters = chapters;
            ChapterListAdapter adapter = new ChapterListAdapter(getActivity(), this.chapters, courseColor);
            chaptersListView.setAdapter(adapter);
        }
    }

    private void onChapterSelected(int index) {
        Bundle args = new Bundle();
        args.putInt(TaskFragment.ARG_COURSE_ID, courseId);
        args.putInt(TaskFragment.ARG_CHAPTER_ID, chapters.get(index).id);

        navigate(R.id.taskFragment, args);
    }
}