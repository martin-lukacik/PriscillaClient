package com.example.priscillaclient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.user.GetUserParams;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.models.User;
import com.example.priscillaclient.views.fragments.browse.AreaCourseFragment;
import com.example.priscillaclient.views.fragments.browse.AreasFragment;
import com.example.priscillaclient.views.fragments.browse.CategoriesFragment;
import com.example.priscillaclient.views.fragments.misc.LeaderboardFragment;
import com.example.priscillaclient.views.fragments.user.ProfileFragment;
import com.example.priscillaclient.views.fragments.user.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements HttpResponse {

    Client client = Client.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(this::onMenuItemSelected);
        navigationView.getMenu().findItem(R.id.menu_dashboard).setChecked(true);

        createActionBar();

        int courseId = getIntent().getIntExtra("course_id", -1);

        if (courseId != -1) {
            Bundle args = new Bundle();
            args.putInt("courseId", courseId);
            navigate(R.id.chaptersFragment, args);
        }
        /*else {
            navigate(new CoursesFragment());
        }*/

        User user = client.user;

        if (user != null)
            setActionBarTitle(user.performance.xp + " XP | " + user.performance.coins + " ©");

        new GetUserParams(this).execute();
    }

    //@SuppressLint("InflateParams")
    private void createActionBar() {
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();/*
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(R.drawable.priscilla_logo_dark);

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.actionbar, null);
            actionBar.setCustomView(v);*/
        }

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
         navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    NavController navController;
    @Override
    public boolean onNavigateUp() {
        return navController.navigateUp() || super.onNavigateUp();
    }

    @Override
    public void onUpdate(Object response) {

        if (response == null)
            return;

        if (response.equals(client.user)) {
            User user = client.user;
            setActionBarTitle(user.performance.xp + " XP | " + user.performance.coins + " ©");
        }
    }

    void setActionBarTitle(String title) {
        TextView tv = findViewById(R.id.actionBarTitle);
        tv.setText(title);
    }

    public void navigate(int layoutId) {
        navigate(layoutId, null);
    }

    public void navigate(int layoutId, Bundle args) {
        NavOptions opts = new NavOptions.Builder()
                .setPopUpTo(layoutId, false, true)
                .setRestoreState(true)
                .build();

        navController.navigate(layoutId, args, opts);
    }

    protected boolean onMenuItemSelected(MenuItem item) {
        /*if (item.getItemId() == R.id.menu_dashboard) {
            navigate(R.id.coursesFragment);
        } else if (item.getItemId() == R.id.menu_all_courses) {
            navigate(R.id.categoriesFragment);
        } else if (item.getItemId() == R.id.menu_leaderboard) {
            navigate(R.id.leaderboardFragment);
        } else if (item.getItemId() == R.id.menu_profile) {
            navigate(R.id.profileFragment);
        }*/
        return true;
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
}