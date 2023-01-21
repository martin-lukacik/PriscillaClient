package com.example.priscillaclient;

import android.os.Bundle;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.user.GetUserParams;
import com.example.priscillaclient.views.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends ActivityBase implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(this::onMenuItemSelected);
        navigationView.getMenu().findItem(R.id.menu_profile).setChecked(true);

        swapFragment(new ProfileFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().findItem(R.id.menu_profile).setChecked(true);
    }

    @Override
    public void onUpdate(Object response) {
        super.onUpdate(response);
    }
}