package com.example.priscillaclient.views.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priscillaclient.LoginActivity;
import com.example.priscillaclient.R;
import com.example.priscillaclient.api.user.GetUserParams;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.models.User;

import java.util.Random;

public class ProfileFragment extends FragmentBase {

    public ProfileFragment() { }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new GetUserParams(this).execute();
    }

    public void logout(View view) {
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", null);
        editor.putString("refresh_token", null);
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void showProfileSettings(View view) {
        swapFragment(new SettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onUpdate(Object response) {

        TextView profileLogout = findViewById(R.id.profileLogoutButton);
        profileLogout.setOnClickListener(this::logout);

        TextView profileSettings = findViewById(R.id.profileSettingsButton);
        profileSettings.setOnClickListener(this::showProfileSettings);

        TextView usernameShort = findViewById(R.id.usernameShort);
        TextView usernameFull = findViewById(R.id.usernameFull);

        User user = Client.getInstance().user;

        usernameShort.setText(user.name.charAt(0) + "" + user.surname.charAt(0));
        usernameFull.setText(user.name + " " + user.surname);

        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        int color = Color.argb(255, r, g, b);

        GradientDrawable drawable = (GradientDrawable) usernameShort.getBackground();
        drawable.mutate();
        drawable.setStroke(5, color);
        usernameShort.setTextColor(color);

        TextView profileLevel = findViewById(R.id.profileLevel);
        TextView profileXP = findViewById(R.id.profileXP);
        TextView profileCoins = findViewById(R.id.profileCoins);
        TextView profileEmail = findViewById(R.id.profileEmail);

        profileLevel.setText(user.performance.level + "");
        profileXP.setText(user.performance.xp + "");
        profileCoins.setText(user.performance.coins + "");
        profileEmail.setText(user.email);
    }
}