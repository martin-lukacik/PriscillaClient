package com.example.priscillaclient.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.GetActiveChapters;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.models.Chapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChapterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChapterFragment extends FragmentBase {

    private static final String ARG_COURSE_ID = "courseId";

    private int courseId;

    public ChapterFragment() { }

    public static ChapterFragment newInstance(int courseId) {
        ChapterFragment fragment = new ChapterFragment();
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

        ListView chaptersListView = findViewById(R.id.chapterListView);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, chaps);
        chaptersListView.setAdapter(adapter);
        chaptersListView.setOnItemClickListener(this::onItemClick);
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
/*
        Client client = Client.getInstance();

        Intent intent = new Intent(getActivity(), TaskActivity.class);
        intent.putExtra("chapter_id", client.chapters.get(i).id);
        intent.putExtra("course_id", courseId);
        startActivity(intent);*/

        int chapterId = chapters.get(i).id;
        swapFragment(TaskFragment.newInstance(courseId, chapterId));

        // , currentLesson, currentLessonId, currentTask

        //Toast.makeText(getActivity(), "Chapter ID " + Client.getInstance().chapters.get(i).id, Toast.LENGTH_SHORT).show();
    }
}