package com.example.priscillaclient.views.fragments.misc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.misc.GetLeaders;
import com.example.priscillaclient.models.Category;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.models.LeaderboardItem;
import com.example.priscillaclient.views.adapters.LeaderboardAdapter;
import com.example.priscillaclient.views.fragments.FragmentBase;

import java.util.ArrayList;

public class LeaderboardFragment extends FragmentBase {

    LeaderboardAdapter adapter;

    public LeaderboardFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new GetLeaders(this).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<LeaderboardItem> leaders = Client.getInstance().leaderboard;
        if (!leaders.isEmpty())
            onUpdate(leaders);
    }

    @Override
    public void onUpdate(Object response) {
        adapter = new LeaderboardAdapter(getActivity(), Client.getInstance().leaderboard);
        ListView lv = findViewById(R.id.leaderboardList);

        if (lv != null)
            lv.setAdapter(adapter);
    }
}