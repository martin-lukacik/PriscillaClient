package com.example.priscillaclient.fragments.app;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.adapters.ChapterListAdapter;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.viewmodels.app.ChaptersViewModel;
import com.example.priscillaclient.viewmodels.app.models.Chapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChaptersFragment extends FragmentBase {

    public static final String ARG_COURSE_ID = "courseId";
    public static final String ARG_COURSE_COLOR = "courseColor";

    ChapterListAdapter adapter;
    private ArrayList<Chapter> chapters = new ArrayList<>();
    private int courseId;
    private int courseColor;

    public ChaptersFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_chapter;

        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
            courseColor = getArguments().getInt(ARG_COURSE_COLOR);
        }

        ChaptersViewModel viewModel = (ChaptersViewModel) getViewModel(ChaptersViewModel.class);
        viewModel.getData().observe(this, (data) -> {
            if (viewModel.hasError())
                showError(viewModel.getError());
            else if (data != null)
                onUpdate(data);
        });
        viewModel.fetchData(courseId);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridView chaptersListView = findViewById(R.id.chapterListView);

        View emptyView = View.inflate(getContext(), R.layout.loading_view, null);
        requireActivity().addContentView(emptyView, chaptersListView.getLayoutParams());
        chaptersListView.setEmptyView(emptyView);
    }

    public void onUpdate(ArrayList<Chapter> response) {

        chapters = response;

        GridView chaptersListView = findViewById(R.id.chapterListView);
        adapter = new ChapterListAdapter(getActivity(), chapters, courseColor);
        chaptersListView.setAdapter(adapter);
        chaptersListView.setOnItemClickListener(this::onItemClick);
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int chapterId = chapters.get(i).id;
        Bundle args = new Bundle();
        args.putInt(TaskFragment.ARG_COURSE_ID, courseId);
        args.putInt(TaskFragment.ARG_CHAPTER_ID, chapterId);
        navigate(R.id.taskFragment, args);
    }
}