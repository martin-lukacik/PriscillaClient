package com.example.priscillaclient;

import android.os.Bundle;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.views.fragments.CategoriesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CategoryActivity extends ActivityBase implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

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

    @Override
    public void onUpdate(Object response) {
        super.onUpdate(response);
    }
}