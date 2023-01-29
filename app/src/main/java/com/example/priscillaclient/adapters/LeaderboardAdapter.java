package com.example.priscillaclient.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.viewmodels.misc.models.Leader;

import java.util.ArrayList;

public class LeaderboardAdapter extends ArrayAdapter<Leader> {

    final Activity context;
    final ArrayList<Leader> leaders;

    public LeaderboardAdapter(Activity context, ArrayList<Leader> leaders) {
        super(context, R.layout.listview_leaderboard, leaders);
        this.context = context;
        this.leaders = leaders;
    }

    static class ViewHolder {
        TextView position;
        TextView name;
        TextView xp;
    }

    public View getView(int i, View view, ViewGroup parent) {

        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_leaderboard, parent, false);

            holder.position = view.findViewById(R.id.leaderboard_position);
            holder.name = view.findViewById(R.id.leaderboard_name);
            holder.xp = view.findViewById(R.id.leaderboard_xp);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String pos = (i + 1) + ".";
        String exp = leaders.get(i).xp + "";

        holder.position.setText(pos);
        holder.name.setText(leaders.get(i).nickname);
        holder.xp.setText(exp);

        return view;
    }
}
