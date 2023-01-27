package com.example.priscillaclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.priscillaclient.user.viewmodel.ProfileViewModel;
import com.example.priscillaclient.user.viewmodel.SettingsViewModel;
import com.example.priscillaclient.user.viewmodel.UserViewModel;
import com.example.priscillaclient.user.viewmodel.models.Theme;
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

public class MainActivity extends ActivityBase {

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int courseId = getIntent().getIntExtra("course_id", -1);

        if (courseId != -1) {
            Bundle args = new Bundle();
            args.putInt("courseId", courseId);
            navigate(R.id.chaptersFragment, args);
        }

        setupNavigation();

        fetchData();
    }

    private void fetchData() {
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
        profileViewModel.fetchData();
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

    public void onUpdate(User user) {
        if (user == null)
            return;

        setActionBarTitle(user.performance.xp + " XP | " + user.performance.coins + " ©");
        setDarkMode(user.theme_id, true);
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


    @Override
    public void onBackPressed(){
        FragmentManager manager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) manager.findFragmentById(R.id.fragmentContainer);
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

    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}