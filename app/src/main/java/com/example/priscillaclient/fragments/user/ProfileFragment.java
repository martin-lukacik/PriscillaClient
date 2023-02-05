package com.example.priscillaclient.fragments.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.priscillaclient.LoginActivity;
import com.example.priscillaclient.R;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.misc.Preferences;
import com.example.priscillaclient.viewmodels.user.ProfileViewModel;
import com.example.priscillaclient.viewmodels.user.SettingsViewModel;
import com.example.priscillaclient.viewmodels.user.UserViewModel;
import com.example.priscillaclient.viewmodels.user.models.Profile;
import com.example.priscillaclient.viewmodels.user.models.Settings;
import com.example.priscillaclient.viewmodels.user.models.Theme;
import com.example.priscillaclient.viewmodels.user.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends FragmentBase {

    Profile profile;
    Settings settings;

    public ProfileFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_profile;
        UserViewModel userViewModel = getViewModel(UserViewModel.class);
        userViewModel.getData().observe(this, this::onUpdate);
        userViewModel.getErrorState().observe(this, this::showError);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View v = findViewById(R.id.loadingView);
        v.setVisibility(View.GONE);

        SettingsViewModel settingsViewModel = getViewModel(SettingsViewModel.class);
        settingsViewModel.getErrorState().observe(getViewLifecycleOwner(), this::showError);
        settings = settingsViewModel.getData().getValue();

        ProfileViewModel profileViewModel = getViewModel(ProfileViewModel.class);
        profileViewModel.getData().observe(getViewLifecycleOwner(), this::onUpdate);
        profileViewModel.getErrorState().observe(getViewLifecycleOwner(), this::showError);
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

    @SuppressLint("SetTextI18n")
    public void onUpdate(User user) {
        if (user == null)
            return;

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
        ImageView profileCoinsImage = findViewById(R.id.profileCoinsImage);

        profileLevel.setText(user.performance.level + "");
        profileXP.setText(user.performance.xp + "");
        profileCoins.setText(user.performance.coins + "");
        profileEmail.setText(user.email);

        if (Theme.THEME_COLORBLIND == preferences.getInt(Preferences.PREFS_THEME_ID, 1)) {
            profileCoinsImage.setImageTintList(ColorStateList.valueOf(0xff008000));
        }

        FloatingActionButton profileSettings = findViewById(R.id.profileSettingsButton);
        profileSettings.setOnClickListener(this::showProfileSettings);
    }

    public void onUpdate(Profile profile) {
        if (profile == null)
            return;

        this.profile = profile;

        TextView profileYear = findViewById(R.id.profileYear);
        profileYear.setText(String.valueOf(profile.yob));

        TextView profileGroup = findViewById(R.id.profileGroup);
        profileGroup.setText(profile.groups);

        TextView profileNick = findViewById(R.id.profileNick);
        profileNick.setTextColor(0xffffffff);
        profileNick.setText(profile.nickname);

        TextView profileCountry = findViewById(R.id.profileCountry);
        profileCountry.setText(settings.getCountryFromId(profile.country_id).country_name);
    }
}