package com.example.priscillaclient.fragments.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.priscillaclient.LoginActivity;
import com.example.priscillaclient.R;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.viewmodel.user.ProfileViewModel;
import com.example.priscillaclient.viewmodel.user.models.Profile;
import com.example.priscillaclient.viewmodel.user.models.User;
import com.example.priscillaclient.viewmodel.user.UserViewModel;

import java.util.Random;

public class ProfileFragment extends FragmentBase {

    Profile profile;

    public ProfileFragment() { }

    int color = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_profile;

        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        color = Color.argb(255, r, g, b);

        UserViewModel userViewModel = (UserViewModel) getViewModel(UserViewModel.class);
        userViewModel.getData().observe(this, this::onUpdate);

        ProfileViewModel profileViewModel = (ProfileViewModel) getViewModel(ProfileViewModel.class);
        profile = profileViewModel.getData().getValue();
    }

    public void logout(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.log_out);
        builder.setMessage(R.string.settings_reset);
        builder.setPositiveButton(R.string.log_out, (dialog, id) -> {
            // Clear settings
            if (getActivity() != null) {
                SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("settings", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.apply();
            }

            // Redirect to login
            LoginActivity.userLoggedIn = false;
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
        });

        AlertDialog d = builder.create();
        d.show();

        d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xffff0000);
    }

    public void showProfileSettings(View view) {
        navigate(R.id.settingsFragment);
    }

    public void onUpdate(User user) {

        if (user == null)
            return;

        TextView profileLogout = findViewById(R.id.profileLogoutButton);
        profileLogout.setOnClickListener(this::logout);

        TextView profileSettings = findViewById(R.id.profileSettingsButton);
        profileSettings.setOnClickListener(this::showProfileSettings);

        TextView usernameShort = findViewById(R.id.usernameShort);
        TextView usernameFull = findViewById(R.id.usernameFull);

        char firstName = 0;
        char lastName = 0;

        if (!user.name.isEmpty())
            firstName = user.name.charAt(0);
        if (!user.surname.isEmpty())
            lastName = user.surname.charAt(0);
        if (firstName == 0 && lastName == 0) {
            if (profile != null && !profile.nickname.isEmpty()) {
                firstName = profile.nickname.charAt(0);
            }
        }

        usernameShort.setText(firstName + "" + lastName);
        usernameFull.setText(user.name + " " + user.surname);

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