package com.example.priscillaclient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.priscillaclient.views.fragments.CoursesFragment;
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

        swapFragment(new CoursesFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().findItem(R.id.menu_dashboard).setChecked(true);
    }

    private void swapFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }
}