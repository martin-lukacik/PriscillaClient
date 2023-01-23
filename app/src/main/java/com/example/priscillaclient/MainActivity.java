package com.example.priscillaclient;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.priscillaclient.views.fragments.app.ChaptersFragment;
import com.example.priscillaclient.views.fragments.app.CoursesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends ActivityBase {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(this::onMenuItemSelected);
        navigationView.getMenu().findItem(R.id.menu_dashboard).setChecked(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }

        int courseId = getIntent().getIntExtra("course_id", -1);

        if (courseId != -1) {
            swapFragment(ChaptersFragment.newInstance(courseId));
        } else {
            swapFragment(new CoursesFragment());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().findItem(R.id.menu_dashboard).setChecked(true);
    }
}