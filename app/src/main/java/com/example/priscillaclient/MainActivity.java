package com.example.priscillaclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.priscillaclient.fragments.app.ChaptersFragment;
import com.example.priscillaclient.misc.Preferences;
import com.example.priscillaclient.viewmodels.user.ProfileViewModel;
import com.example.priscillaclient.viewmodels.user.SettingsViewModel;
import com.example.priscillaclient.viewmodels.user.UserViewModel;
import com.example.priscillaclient.viewmodels.user.models.Language;
import com.example.priscillaclient.viewmodels.user.models.Settings;
import com.example.priscillaclient.viewmodels.user.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends ActivityBase {

    public static final String INTENT_COURSE_ID = "courseId";

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView actionBarCoins = findViewById(R.id.actionBarCoins);
        actionBarCoins.setColorFilter(0xffffffff);

        int courseId = getIntent().getIntExtra(INTENT_COURSE_ID, -1);

        if (courseId != -1) {
            Bundle args = new Bundle();
            args.putInt(INTENT_COURSE_ID, courseId);
            navigate(R.id.chaptersFragment, args);
        }

        setupNavigation();

        fetchData();
    }

    private void fetchData() {
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getData().observe(this, (data) -> {
            initialUpdate = true;
            onUpdate(data);
        });
        userViewModel.getErrorState().observe(this, this::showError);
        userViewModel.fetchData();

        ProfileViewModel profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.fetchData();

        SettingsViewModel settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsViewModel.fetchData();
        settingsViewModel.getData().observe(this, this::onUpdate);
    }

    private void setupNavigation() {
        FragmentManager manager = getSupportFragmentManager();
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigation);
        NavHostFragment navHostFragment = (NavHostFragment) manager.findFragmentById(R.id.fragmentContainer);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(navigationView, navController);
        }
    }

    @Override
    public boolean onNavigateUp() {
        return navController.navigateUp() || super.onNavigateUp();
    }

    boolean initialUpdate = true;
    public void onUpdate(User user) {
        if (user == null || !initialUpdate)
            return;

        setActionBarTitle(user.performance.xp + " XP   " + user.performance.coins);

        setDarkMode(user.theme_id, true, true);
        initialUpdate = false;
    }

    public void onUpdate(Settings settings) {
        if (settings == null || settings.isEmpty())
            return;

        ProfileViewModel profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.getData().observe(this, (d) -> {

            if (d == null)
                return;

            String currentShortcut = preferences.getString(Preferences.PREFS_LANGUAGE_SHORTCUT, "en");
            String shortcut = "en";

            for (Language language : settings.languages) {
                if (language.id == d.pref_lang_id) {
                    shortcut = language.shortcut.toLowerCase();
                    break;
                }
            }

            if (!shortcut.equals(currentShortcut)) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.PREFS_LANGUAGE_SHORTCUT, shortcut);
                editor.apply();
                changeLocale(shortcut, true);
            }
        });
    }

    void setActionBarTitle(String title) {
        TextView tv = findViewById(R.id.actionBarTitle);
        tv.setText(title);
    }

    public void navigate(int layoutId, Bundle args) {
        navController.navigate(layoutId, args);
    }

    @Override
    protected void onNewIntent(Intent intent) { // TODO dead code?
        super.onNewIntent(intent);

        int courseId = intent.getIntExtra(INTENT_COURSE_ID, -1);

        if (courseId != -1) {
            Bundle args = new Bundle();
            args.putInt(ChaptersFragment.ARG_COURSE_ID, courseId);
            navigate(R.id.chaptersFragment, args);
        }
    }

    @Override
    public void onBackPressed(){
        FragmentManager manager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) manager.findFragmentById(R.id.fragmentContainer);

        if (navHostFragment != null) {
            FragmentManager fm = navHostFragment.getChildFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                super.onBackPressed();
            } else {
                // Prevents login screen on return to the app
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
            }
        }
    }
}