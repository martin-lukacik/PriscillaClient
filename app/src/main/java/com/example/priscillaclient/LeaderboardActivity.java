package com.example.priscillaclient;

import android.os.Bundle;
import android.widget.ListView;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.misc.GetLeaders;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.views.adapters.LeaderboardAdapter;
import com.example.priscillaclient.views.fragments.misc.LeaderboardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LeaderboardActivity extends ActivityBase implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(this::onMenuItemSelected);
        navigationView.getMenu().findItem(R.id.menu_leaderboard).setChecked(true);

        swapFragment(new LeaderboardFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().findItem(R.id.menu_leaderboard).setChecked(true);
    }
}