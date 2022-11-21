package com.example.priscillaclient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.priscillaclient.models.LeaderboardItem;

import java.util.ArrayList;

public class LeaderboardAdapter extends ArrayAdapter<LeaderboardItem> {

    final Activity context;
    final ArrayList<LeaderboardItem> leaders;

    public LeaderboardAdapter(Activity context, ArrayList<LeaderboardItem> leaders) {
        super(context, R.layout.listview_leaderboard, leaders);
        this.context = context;
        this.leaders = leaders;
    }

    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_leaderboard, null,true);

        TextView position = rowView.findViewById(R.id.leaderboard_position);
        TextView name = rowView.findViewById(R.id.leaderboard_name);
        TextView xp = rowView.findViewById(R.id.leaderboard_xp);

        position.setText((i + 1) + ".");
        name.setText(leaders.get(i).nickname);
        xp.setText("" + leaders.get(i).xp);

        return rowView;
    }
}
