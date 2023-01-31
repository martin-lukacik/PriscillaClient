package com.example.priscillaclient.fragments.misc;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.adapters.LeaderboardAdapter;
import com.example.priscillaclient.fragments.FragmentAdapter;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.viewmodels.misc.LeadersViewModel;
import com.example.priscillaclient.viewmodels.misc.models.Leader;
import com.example.priscillaclient.viewmodels.user.SettingsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LeaderboardFragment extends FragmentBase implements FragmentAdapter<ArrayList<Leader>> {

    LeaderboardAdapter adapter;

    int index = 0;
    int top = 0;

    ListView leaderboardListview;

    public void onPause() {
        super.onPause();
        View v = leaderboardListview.getChildAt(0);
        index = leaderboardListview.getFirstVisiblePosition();
        top = (v == null ? 0 : (v.getTop() - leaderboardListview.getPaddingTop()));
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", index);
        outState.putInt("top", top);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_leaderboard;

        LeadersViewModel viewModel = getViewModel(LeadersViewModel.class);
        viewModel.getData().observe(this, onResponse(viewModel.getError()));
        viewModel.fetchData();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEmptyView(findViewById(R.id.leaderboardList));

        if (savedInstanceState != null) {
            index = savedInstanceState.getInt("index", 0);
            top = savedInstanceState.getInt("top", 0);
        }

        leaderboardListview = findViewById(R.id.leaderboardList);
    }

    @Override
    public void onUpdate(ArrayList<Leader> response) {
        SettingsViewModel viewModel = getViewModel(SettingsViewModel.class);

        if (viewModel.getData().getValue() != null && leaderboardListview != null) {
            adapter = new LeaderboardAdapter(getActivity(), response, viewModel.getData().getValue().countries);
            leaderboardListview.setAdapter(adapter);
            leaderboardListview.setSelectionFromTop(index, top);
        }
    }
}