package com.example.priscillaclient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.user.GetUserParams;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.models.User;
import com.example.priscillaclient.views.fragments.app.ChaptersFragment;
import com.example.priscillaclient.views.fragments.app.CoursesFragment;
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
            navigate(ChaptersFragment.newInstance(courseId));
        } else {
            navigate(new CoursesFragment());
        }

        User user = client.user;

        if (user != null)
            setActionBarTitle(user.performance.xp + " XP | " + user.performance.coins + " ©");

        new GetUserParams(this).execute();
    }

    @SuppressLint("InflateParams")
    private void createActionBar() {
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(R.drawable.priscilla_logo_dark);

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.actionbar, null);
            actionBar.setCustomView(v);
        }
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

    boolean firstFragment = true;
    public void navigate(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();

        if (firstFragment)
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.fragmentContainerView, fragment)
                    .commit();
        else
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.fragmentContainerView, fragment)
                    .addToBackStack(null)
                    .commit();

        firstFragment = false;

        higlightMenuFromFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainerView);

        higlightMenuFromFragment(fragment);
    }

    private int getMenuItemFromFragment(Fragment fragment) {

        if (fragment instanceof AreaCourseFragment
                || fragment instanceof AreasFragment
                || fragment instanceof CategoriesFragment) {
            return R.id.menu_all_courses;
        } else if (fragment instanceof LeaderboardFragment) {
            return R.id.menu_leaderboard;
        } else if (fragment instanceof ProfileFragment
                || fragment instanceof SettingsFragment) {
            return R.id.menu_profile;
        }

        return R.id.menu_dashboard;
    }

    boolean highlightingMenu = false;
    protected void higlightMenuFromFragment(Fragment fragment) {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        highlightingMenu = true;
        bottomNavigation.setSelectedItemId(getMenuItemFromFragment(fragment));
        highlightingMenu = false;
    }

    protected boolean onMenuItemSelected(MenuItem item) {
        if (highlightingMenu)
            return true;

        if (item.getItemId() == R.id.menu_dashboard) {
            navigate(new CoursesFragment());
        } else if (item.getItemId() == R.id.menu_all_courses) {
            navigate(new CategoriesFragment());
        } else if (item.getItemId() == R.id.menu_leaderboard) {
            navigate(new LeaderboardFragment());
        } else if (item.getItemId() == R.id.menu_profile) {
            navigate(new ProfileFragment());
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int courseId = intent.getIntExtra("course_id", -1);

        if (courseId != -1) {
            navigate(ChaptersFragment.newInstance(courseId));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().findItem(R.id.menu_dashboard).setChecked(true);
    }
}