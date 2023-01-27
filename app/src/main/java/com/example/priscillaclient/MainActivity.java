package com.example.priscillaclient;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.priscillaclient.user.viewmodel.ProfileViewModel;
import com.example.priscillaclient.user.viewmodel.SettingsViewModel;
import com.example.priscillaclient.user.viewmodel.UserViewModel;
import com.example.priscillaclient.user.viewmodel.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
 * TODO Loading should show dialogs and/or clear previous state (Courses -> Chapters -> Tasks shows old data while loading)
 * TODO Dark mode
 * TODO Colorblind mode?
 * TODO Submit task resets inputs
 * TODO Proper multi-language support based on app setting
 * TODO ViewPager for task navigation
 * TODO Implement TaskType.TASK_CODE
 */

public class MainActivity extends AppCompatActivity {

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        SharedPreferences settings = getSharedPreferences("settings", 0);

        int theme_id = settings.getInt("theme_id", 0);
        int theme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        if (theme_id == 1) {
            theme = AppCompatDelegate.MODE_NIGHT_NO;
        } else if (theme_id == 2) {
            theme = AppCompatDelegate.MODE_NIGHT_YES;
        }
        AppCompatDelegate.setDefaultNightMode(theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }

        int courseId = getIntent().getIntExtra("course_id", -1);

        if (courseId != -1) {
            Bundle args = new Bundle();
            args.putInt("courseId", courseId);
            navigate(R.id.chaptersFragment, args);
        }

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(navigationView, navController);
        }

        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getData().observe(this, (data) -> {
            if (userViewModel.hasError())
                showError(userViewModel.getError());
            else
                onUpdate(data);
        });
        userViewModel.fetchData();

        SettingsViewModel settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsViewModel.fetchData();

        ProfileViewModel profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.getData().observe(this, (data) -> {
            if (data != null) {
                int mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                if (data.theme_id == 1) {
                    mode = AppCompatDelegate.MODE_NIGHT_NO;
                } else if (data.theme_id == 2) {
                    mode = AppCompatDelegate.MODE_NIGHT_YES;
                }
                if (AppCompatDelegate.getDefaultNightMode() != mode)
                    AppCompatDelegate.setDefaultNightMode(mode);

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("theme_id", mode);
                editor.apply();
                themeSet = true;
            }
        });
        profileViewModel.fetchData();
    }

    boolean themeSet = false;

    @Override
    public boolean onNavigateUp() {
        return navController.navigateUp() || super.onNavigateUp();
    }

    public void onUpdate(User user) {
        if (user != null)
            setActionBarTitle(user.performance.xp + " XP | " + user.performance.coins + " ©");
    }

    void setActionBarTitle(String title) {
        TextView tv = findViewById(R.id.actionBarTitle);
        tv.setText(title);
    }

    public void navigate(int layoutId, Bundle args) {
        navController.navigate(layoutId, args);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int courseId = intent.getIntExtra("course_id", -1);

        if (courseId != -1) {
            Bundle args = new Bundle();
            args.putInt("courseId", courseId);
            navigate(R.id.chaptersFragment, args);
        }
    }

    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}