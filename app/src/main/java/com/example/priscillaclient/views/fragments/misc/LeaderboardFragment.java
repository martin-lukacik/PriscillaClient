package com.example.priscillaclient.views.fragments.misc;

import android.os.Bundle;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.misc.GetLeaders;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.views.adapters.LeaderboardAdapter;
import com.example.priscillaclient.views.fragments.FragmentBase;

public class LeaderboardFragment extends FragmentBase implements HttpResponse<Object> {

    LeaderboardAdapter adapter;

    public LeaderboardFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_leaderboard;

        new GetLeaders(this).execute();
    }

    @Override
    public void onUpdate(Object response) {
        adapter = new LeaderboardAdapter(getActivity(), Client.getInstance().leaderboard);
        ListView lv = findViewById(R.id.leaderboardList);

        if (lv != null)
            lv.setAdapter(adapter);
    }
}