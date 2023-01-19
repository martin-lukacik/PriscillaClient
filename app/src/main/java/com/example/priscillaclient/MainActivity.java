package com.example.priscillaclient;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.priscillaclient.api.GetUserCourses;
import com.example.priscillaclient.api.GetUserParams;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.fragments.CoursesFragment;
import com.example.priscillaclient.models.Course;
import com.example.priscillaclient.models.User;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.fragmentContainerView, new CoursesFragment());
        transaction.addToBackStack(null);
        transaction.commit();

        new GetUserParams(this).execute();
        new GetUserCourses(this).execute();
    }

    CourseListAdapter adapter;

    @Override
    public void onUpdate(Object response) {

        super.onUpdate(response);

        if (response instanceof User) {
            super.setMenuTitle((User) response);
        }

        else if (response instanceof ArrayList<?>) {
            courses = Client.getInstance().courses;

            SharedPreferences settings = getApplicationContext().getSharedPreferences(PREF_SET, 0);
            int pinnedCourseId = settings.getInt("pinnedCourseId", -1);

            if (pinnedCourseId != -1) {
                int index = -1;

                for (int i = 0; i < courses.size(); ++i) {
                    if (pinnedCourseId == courses.get(i).course_id) {
                        index = i;
                        break;
                    }
                }

                Course course = courses.remove(index);
                course.isPinned = true;
                courses.add(0, course);
            }
            Toast.makeText(this, pinnedCourseId + " pinned update", Toast.LENGTH_SHORT).show();

            GridView courseListView = findViewById(R.id.courseListView);
            adapter = new CourseListAdapter(this, courses);
            courseListView.setAdapter(adapter);
            courseListView.setOnItemClickListener(this::courseSelected);
            courseListView.setOnItemLongClickListener(this::coursePinned);
        }
    }

    ArrayList<Course> courses = new ArrayList<>();
    final String PREF_SET = "settings";
    private boolean coursePinned(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO clear this up
        Course course = courses.get(i);

        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREF_SET, 0);
        int pinnedCourseId = settings.getInt("pinnedCourseId", -1);

        if (pinnedCourseId == -1) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("pinnedCourseId", courses.get(i).course_id);
            editor.apply();
            courses.remove(i);
            courses.add(0, course);
            course.isPinned = true;
            adapter.notifyDataSetChanged();
        } else if (course.course_id == pinnedCourseId) {
            course.isPinned = false;
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("pinnedCourseId", -1);
            editor.apply();
            courses = new ArrayList<>(Client.getInstance().courses);
            adapter = new CourseListAdapter(this, courses);
            GridView courseListView = findViewById(R.id.courseListView);
            courseListView.setAdapter(adapter);
        }

        return true;
    }

    private void courseSelected(AdapterView<?> adapterView, View view, int i, long l){
        Bundle bundle = new Bundle();
        int courseId = courses.get(i).course_id;
        bundle.putInt("course_id", courseId);

        Intent intent = new Intent(MainActivity.this, ChapterActivity.class);
        intent.putExtra("course_id", courseId);
        startActivity(intent);
    }
}