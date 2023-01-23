package com.example.priscillaclient;

import android.os.Bundle;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.views.fragments.browse.CategoriesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BrowseActivity extends ActivityBase implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(this::onMenuItemSelected);
        navigationView.getMenu().findItem(R.id.menu_all_courses).setChecked(true);

        swapFragment(new CategoriesFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().findItem(R.id.menu_all_courses).setChecked(true);
    }
}