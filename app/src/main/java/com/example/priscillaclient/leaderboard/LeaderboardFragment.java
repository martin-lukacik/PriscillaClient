package com.example.priscillaclient.leaderboard;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.util.FragmentBase;
import com.example.priscillaclient.leaderboard.viewmodel.models.LeaderboardItem;
import com.example.priscillaclient.leaderboard.viewmodel.LeadersViewModel;
import com.example.priscillaclient.util.LoadingDialog;
import com.example.priscillaclient.util.adapters.LeaderboardAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LeaderboardFragment extends FragmentBase {

    LeaderboardAdapter adapter;
    LoadingDialog dialog;

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

        dialog = new LoadingDialog(getActivity());

        LeadersViewModel viewModel = (LeadersViewModel) getViewModel(LeadersViewModel.class);
        viewModel.getData().observe(this, (data) -> {
            if (viewModel.hasError()) {
                dialog.dismiss();
                showError(viewModel.getError());
            }
            else
                onUpdate(data);
            ListView lv = findViewById(R.id.leaderboardList);
            lv.setSelectionFromTop(index, top);
        });

        if (viewModel.getData().getValue().isEmpty()) {
            dialog.show();
        }

        viewModel.fetchData();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            index = savedInstanceState.getInt("index", 0);
            top = savedInstanceState.getInt("top", 0);
        }
    }

    boolean firstCall = true;
    public void onUpdate(ArrayList<LeaderboardItem> response) {
        adapter = new LeaderboardAdapter(getActivity(), response);
        ListView lv = findViewById(R.id.leaderboardList);

        if (lv != null)
            lv.setAdapter(adapter);

        if (!firstCall) {
            dialog.dismiss();
        } else {
            firstCall = false;
        }

    }
}