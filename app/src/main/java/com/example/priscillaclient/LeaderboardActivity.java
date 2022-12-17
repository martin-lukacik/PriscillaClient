package com.example.priscillaclient;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.priscillaclient.api.GetLeaders;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.LeaderboardItem;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        new GetLeaders(this).execute();
    }

    LeaderboardAdapter adapter;

    @Override
    public void onUpdate(Object response) {
        ArrayList<LeaderboardItem> leaders = (ArrayList<LeaderboardItem>) response;

        adapter = new LeaderboardAdapter(this, leaders);

        ListView lv = findViewById(R.id.leaderboardList);
        lv.setAdapter(adapter);
    }
}