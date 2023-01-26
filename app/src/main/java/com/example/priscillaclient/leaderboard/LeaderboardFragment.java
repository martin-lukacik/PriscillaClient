package com.example.priscillaclient.leaderboard;

import android.os.Bundle;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.util.FragmentBase;
import com.example.priscillaclient.leaderboard.viewmodel.models.LeaderboardItem;
import com.example.priscillaclient.leaderboard.viewmodel.LeadersViewModel;
import com.example.priscillaclient.util.LoadingDialog;
import com.example.priscillaclient.util.adapters.LeaderboardAdapter;

import java.util.ArrayList;

public class LeaderboardFragment extends FragmentBase {

    LeaderboardAdapter adapter;
    LoadingDialog dialog;

    public LeaderboardFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_leaderboard;

        dialog = new LoadingDialog(getActivity());

        LeadersViewModel viewModel = (LeadersViewModel) getViewModel(LeadersViewModel.class);
        viewModel.getData().observe(this, (data) -> {
            if (viewModel.hasError())
                showError(viewModel.getError());
            else
                onUpdate(data);
        });

        if (viewModel.getData().getValue().isEmpty()) {
            dialog.show();
        }

        viewModel.fetchData();
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