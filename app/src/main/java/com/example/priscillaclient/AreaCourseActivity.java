package com.example.priscillaclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.api.GetAreaCourses;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.client.Client;
import com.example.priscillaclient.models.AreaCourse;

import java.util.ArrayList;

public class AreaCourseActivity extends AppCompatActivity implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_course);

        Intent intent = getIntent();

        int areaId = intent.getIntExtra("area_id", -1);

        new GetAreaCourses(this, areaId).execute();
    }

    @Override
    public void onUpdate(Object response) {
        ArrayList<AreaCourse> areaCourses = Client.getInstance().areaCourses;

        ArrayAdapter<AreaCourse> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, areaCourses);
        ListView areaCourseList = findViewById(R.id.areaCourseList);
        areaCourseList.setAdapter(adapter);
        areaCourseList.setOnItemClickListener(this::courseSelected);
    }

    private void courseSelected(AdapterView<?> adapterView, View view, int i, long l) {
        AreaCourse course = Client.getInstance().areaCourses.get(i);
        if (course.status == AreaCourse.CourseStatus.OPENED) {
            Intent intent = new Intent(AreaCourseActivity.this, MainActivity.class /* TODO ChapterActivity.class */);
            intent.putExtra("course_id", course.id);
            startActivity(intent);
        }

        // TODO else if status null? or not opened, dialog to enroll
    }
}