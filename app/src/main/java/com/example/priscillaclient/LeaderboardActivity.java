package com.example.priscillaclient;

import android.os.Bundle;
import android.widget.ListView;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.misc.GetLeaders;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.views.adapters.LeaderboardAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LeaderboardActivity extends ActivityBase implements HttpResponse {

    LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(this::onMenuItemSelected);
        navigationView.getMenu().findItem(R.id.menu_leaderboard).setChecked(true);

        new GetLeaders(this).execute();
    }

    @Override
    public void onUpdate(Object response) {
        adapter = new LeaderboardAdapter(this, Client.getInstance().leaderboard);
        ListView lv = findViewById(R.id.leaderboardList);

        if (lv != null)
            lv.setAdapter(adapter);
    }
}