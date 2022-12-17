package com.example.priscillaclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.api.GetActiveChapters;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.models.Chapter;

import java.util.ArrayList;

public class ChapterActivity extends AppCompatActivity implements HttpResponse {

    int courseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        Intent intent = getIntent();
        courseId = intent.getExtras().getInt("course_id", -1);
        Log.i("Course ID", "=" + courseId);

        new GetActiveChapters(this, courseId).execute();

        // https://app.priscilla.fitped.eu/get-active-chapters2/{COURSE_ID}
    }

    ArrayAdapter<String> adapter;
    @Override
    public void onUpdate(Object response) {

        if (response == null)
            return;

        ArrayList<Chapter> chapters = Client.getInstance().chapters;

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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chaps);
        chaptersListView.setAdapter(adapter);
        chaptersListView.setOnItemClickListener(this::onItemClick);
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Client client = Client.getInstance();

        Intent intent = new Intent(ChapterActivity.this, TaskActivity.class);
        intent.putExtra("chapter_id", client.chapters.get(i).id);
        intent.putExtra("course_id", courseId);
        startActivity(intent);
    }
}