package com.example.priscillaclient.fragments.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.priscillaclient.LoginActivity;
import com.example.priscillaclient.R;
import com.example.priscillaclient.fragments.FragmentAdapter;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.util.Preferences;
import com.example.priscillaclient.viewmodels.user.ProfileViewModel;
import com.example.priscillaclient.viewmodels.user.SettingsViewModel;
import com.example.priscillaclient.viewmodels.user.models.Country;
import com.example.priscillaclient.viewmodels.user.models.Profile;
import com.example.priscillaclient.viewmodels.user.models.Settings;
import com.example.priscillaclient.viewmodels.user.models.User;
import com.example.priscillaclient.viewmodels.user.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ProfileFragment extends FragmentBase implements FragmentAdapter<User> {

    Profile profile;
    Settings settings;

    public ProfileFragment() { }

    int color = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_profile;

        Random rand = new Random();
        color = Color.parseColor(colors[rand.nextInt(colors.length)]);

        UserViewModel userViewModel = (UserViewModel) getViewModel(UserViewModel.class);
        userViewModel.getData().observe(this, onResponse(userViewModel));

        ProfileViewModel profileViewModel = (ProfileViewModel) getViewModel(ProfileViewModel.class);
        profile = profileViewModel.getData().getValue();
        profileViewModel.getData().observe(this, (data) -> {
            profile = data;
            onUpdateProfile();
        });

        SettingsViewModel settingsViewModel = (SettingsViewModel) getViewModel(SettingsViewModel.class);
        settings = settingsViewModel.getData().getValue();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View v = findViewById(R.id.loadingView);
        v.setVisibility(View.GONE);

        onUpdateProfile();

        LinearLayout profileHeader = findViewById(R.id.profileHeader);
        profileHeader.setBackgroundColor(color);
    }

    public void logout(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.log_out);
        builder.setMessage(R.string.settings_reset);
        builder.setPositiveButton(R.string.log_out, (dialog, id) -> {
            // Clear settings
            if (getActivity() != null) {
                SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(Preferences.PREFS, 0);
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

    String[] colors = new String[] {
        "#0085FF",
        "#008A34",
        "#F98600",
        "#E92C2C",
    };

    @SuppressLint("SetTextI18n")
    public void onUpdate(User user) {

        TextView profileLogout = findViewById(R.id.profileLogoutButton);
        profileLogout.setOnClickListener(this::logout);


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

        String fullName = user.name + " " + user.surname;
        String shortName = firstName + "" + lastName;
        usernameFull.setText(fullName);
        usernameShort.setText(shortName);

        // Color the text
        //usernameShort.setTextColor(color);

        /*usernameShort.setBackgroundColor(color);*/
        usernameShort.setTextColor(0xffffffff);
        // Color the circle
        GradientDrawable drawable = (GradientDrawable) usernameShort.getBackground();
        drawable.mutate();
        drawable.setStroke(5, 0xffffffff);

        usernameFull.setTextColor(0xffffffff);

        TextView profileLevel = findViewById(R.id.profileLevel);
        TextView profileXP = findViewById(R.id.profileXP);
        TextView profileCoins = findViewById(R.id.profileCoins);
        TextView profileEmail = findViewById(R.id.profileEmail);

        profileLevel.setText(user.performance.level + "");
        profileXP.setText(user.performance.xp + "");
        profileCoins.setText(user.performance.coins + "");
        profileEmail.setText(user.email);


        LinearLayout alternateRow = findViewById(R.id.alternateRow);
        alternateRow.setBackgroundColor(0xfff1f1f1);

        FloatingActionButton profileSettings = findViewById(R.id.profileSettingsButton);
        profileSettings.setOnClickListener(this::showProfileSettings);
        profileSettings.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void onUpdateProfile() {
        TextView profileYear = findViewById(R.id.profileYear);
        profileYear.setText(profile.yob + "");

        TextView profileGroup = findViewById(R.id.profileGroup);
        profileGroup.setText(profile.groups + "");

        TextView profileNick = findViewById(R.id.profileNick);
        profileNick.setTextColor(0xffffffff);
        profileNick.setText(profile.nickname + "");

        TextView profileCountry = findViewById(R.id.profileCountry);
        profileCountry.setText(settings.getCountryFromId(profile.country_id).country_name + "");
    }
}