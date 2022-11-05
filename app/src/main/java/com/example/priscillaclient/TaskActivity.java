package com.example.priscillaclient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.priscillaclient.api.GetActiveLessons;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.Lesson;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Intent intent = getIntent();

        new GetActiveLessons(this, intent.getIntExtra("chapter_id", -1)).execute();
    }

    ArrayAdapter<Lesson> adapter;
    @Override
    public void onUpdate(Object response) {

        ArrayList<Lesson> lessons = (ArrayList<Lesson>) response;

        // TODO Collections.sort(agentDtoList, (o1, o2) -> o1.getCustomerCount() - o2.getCustomerCount());

        TextView previousLesson = findViewById(R.id.previousLesson);
        TextView currentLesson = findViewById(R.id.currentLesson);
        TextView nextLesson = findViewById(R.id.nextLesson);

        if (lessons.get(0) != null)
            previousLesson.setText(lessons.get(0).toString());
        if (lessons.get(1) != null)
            currentLesson.setText(lessons.get(1).toString());
        if (lessons.get(2) != null)
            nextLesson.setText(lessons.get(2).toString());
    }
}