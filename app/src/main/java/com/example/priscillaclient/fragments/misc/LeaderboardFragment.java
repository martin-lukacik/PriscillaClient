package com.example.priscillaclient.fragments.misc;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.adapters.LeaderboardAdapter;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.viewmodels.misc.LeadersViewModel;
import com.example.priscillaclient.viewmodels.misc.models.LeaderboardItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LeaderboardFragment extends FragmentBase {

    LeaderboardAdapter adapter;

    public LeaderboardFragment() { }

    int index = 0;
    int top = 0;

    public void onPause() {
        super.onPause();
        ListView lv = findViewById(R.id.leaderboardList);
        View c = lv.getChildAt(0);
        index = lv.getFirstVisiblePosition();
        top = (c == null ? 0 : (c.getTop() - lv.getPaddingTop()));
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

        LeadersViewModel viewModel = (LeadersViewModel) getViewModel(LeadersViewModel.class);
        viewModel.getData().observe(this, (data) -> {
            if (viewModel.hasError())
                showError(viewModel.getError());
            else
                onUpdate(data);
            ListView lv = findViewById(R.id.leaderboardList);
            lv.setSelectionFromTop(index, top);
        });

        viewModel.fetchData();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            index = savedInstanceState.getInt("index", 0);
            top = savedInstanceState.getInt("top", 0);
        }

        ListView lv = findViewById(R.id.leaderboardList);

        View emptyView = View.inflate(getContext(), R.layout.loading_view, null);
        requireActivity().addContentView(emptyView, lv.getLayoutParams());
        lv.setEmptyView(emptyView);
    }

    public void onUpdate(ArrayList<LeaderboardItem> response) {
        adapter = new LeaderboardAdapter(getActivity(), response);
        ListView lv = findViewById(R.id.leaderboardList);

        if (lv != null)
            lv.setAdapter(adapter);

    }
}