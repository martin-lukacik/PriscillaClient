package com.example.priscillaclient.fragments.misc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.priscillaclient.R;
import com.example.priscillaclient.adapters.LeaderboardAdapter;
import com.example.priscillaclient.fragments.FragmentAdapter;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.viewmodels.misc.LeadersViewModel;
import com.example.priscillaclient.viewmodels.misc.models.Leader;
import com.example.priscillaclient.viewmodels.user.SettingsViewModel;
import com.example.priscillaclient.viewmodels.user.models.Settings;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class LeaderboardFragment extends FragmentBase {

    LeaderboardAdapter adapter;

    int index = 0;
    int top = 0;

    SwipeRefreshLayout pullToRefresh;
    ListView leaderboardListview;

    LeadersViewModel viewModel;

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

        viewModel = getViewModel(LeadersViewModel.class);
        viewModel.getData().observe(this, this::onUpdate);
        viewModel.fetchData(false);
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

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            viewModel.fetchData(true);
        });

        // Observe loading state
        viewModel.getLoadingState().observe(getViewLifecycleOwner(), (isLoading) -> {
            pullToRefresh.setRefreshing(isLoading);
        });

        // Observe error state
        viewModel.getErrorState().observe(getViewLifecycleOwner(), this::showError);
    }

    public void onUpdate(ArrayList<Leader> leaders) {
        if (leaders == null)
            return;

        SettingsViewModel viewModel = getViewModel(SettingsViewModel.class);
        Settings settings = viewModel.getData().getValue();

        adapter = new LeaderboardAdapter(getActivity(), leaders, settings);
        leaderboardListview.setAdapter(adapter);
        leaderboardListview.setSelectionFromTop(index, top);
    }
}