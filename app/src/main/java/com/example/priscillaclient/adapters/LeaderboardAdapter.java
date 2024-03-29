package com.example.priscillaclient.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.viewmodels.misc.models.Leader;
import com.example.priscillaclient.viewmodels.user.models.Country;
import com.example.priscillaclient.viewmodels.user.models.Settings;

import java.util.ArrayList;

public class LeaderboardAdapter extends ArrayAdapter<Leader> {

    final Activity context;
    final ArrayList<Leader> leaders;
    final Settings settings;

    public LeaderboardAdapter(Activity context, ArrayList<Leader> leaders, Settings settings) {
        super(context, R.layout.listview_leaderboard, leaders);
        this.context = context;
        this.leaders = leaders;
        this.settings = settings;
    }

    static class ViewHolder {
        TextView position;
        TextView name;
        TextView xp;
        ImageView country;
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
            holder.country = view.findViewById(R.id.leaderboard_country);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Leader leader = leaders.get(i);

        holder.position.setText(String.valueOf(i + 1));
        holder.name.setText(leader.nickname);
        holder.xp.setText(leader.xp + " " + getContext().getString(R.string.xp));

        if (settings != null) {
            for (Country c : settings.countries) {
                if (c.country_name.equals(leader.country)) {
                    int id = getContext().getResources().getIdentifier("flag_" + c.iso_code.toLowerCase(), "drawable", context.getPackageName());
                    holder.country.setImageResource(id);
                    break;
                }
            }
        }

        return view;
    }
}
