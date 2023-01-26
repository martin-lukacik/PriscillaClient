package com.example.priscillaclient.views.fragments.misc;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.lifecycle.ViewModelProviders;

import com.example.priscillaclient.R;
import com.example.priscillaclient.models.LeaderboardItem;
import com.example.priscillaclient.viewmodel.misc.LeadersViewModel;
import com.example.priscillaclient.views.LoadingDialog;
import com.example.priscillaclient.views.adapters.LeaderboardAdapter;
import com.example.priscillaclient.views.fragments.FragmentBase;

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

        LeadersViewModel viewModel = ViewModelProviders.of(this).get(LeadersViewModel.class);
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