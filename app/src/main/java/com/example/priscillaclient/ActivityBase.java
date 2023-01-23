package com.example.priscillaclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.user.GetUserParams;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.models.User;
import com.example.priscillaclient.views.fragments.app.ChaptersFragment;
import com.example.priscillaclient.views.fragments.app.TaskFragment;
import com.example.priscillaclient.views.fragments.browse.AreaCourseFragment;
import com.example.priscillaclient.views.fragments.browse.AreasFragment;
import com.example.priscillaclient.views.fragments.browse.CategoriesFragment;
import com.example.priscillaclient.views.fragments.app.CoursesFragment;
import com.example.priscillaclient.views.fragments.misc.LeaderboardFragment;
import com.example.priscillaclient.views.fragments.user.ProfileFragment;
import com.example.priscillaclient.views.fragments.user.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public abstract class ActivityBase extends AppCompatActivity implements HttpResponse {

    Client client = Client.getInstance();

    ChaptersFragment chaptersFragment;
    CoursesFragment coursesFragment;


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

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setIcon(R.drawable.priscilla_logo_dark);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        /*getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xfff9f9f9));*/

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.priscilla_logo_dark);

        LayoutInflater inflater = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View v = inflater.inflate(R.layout.actionbar, null);

        actionBar.setCustomView(v);

        User user = client.user;

        if (user != null)
            setActionBarTitle(user.performance.xp + " XP | " + user.performance.coins + " ©");

        new GetUserParams(this).execute();
    }

    boolean firstFragment = true;
    public void swapFragment(Fragment fragment) {
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

    boolean highlightingMenu = false;
    protected void higlightMenuFromFragment(Fragment fragment) {
        highlightingMenu = true;
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        if (fragment instanceof ChaptersFragment
                || fragment instanceof CoursesFragment
                || fragment instanceof TaskFragment) {
            bottomNavigation.setSelectedItemId(R.id.menu_dashboard);
        } else if (fragment instanceof AreaCourseFragment
                || fragment instanceof AreasFragment
                || fragment instanceof CategoriesFragment) {
            bottomNavigation.setSelectedItemId(R.id.menu_all_courses);
        } else if (fragment instanceof LeaderboardFragment) {
            bottomNavigation.setSelectedItemId(R.id.menu_leaderboard);
        } else if (fragment instanceof ProfileFragment
                || fragment instanceof SettingsFragment) {
            bottomNavigation.setSelectedItemId(R.id.menu_profile);
        }
        highlightingMenu = false;
    }

    protected boolean onMenuItemSelected(MenuItem item) {
        if (highlightingMenu)
            return true;

        if (item.getItemId() == R.id.menu_dashboard) {
            swapFragment(new CoursesFragment());
        } else if (item.getItemId() == R.id.menu_all_courses) {
            swapFragment(new CategoriesFragment());
        } else if (item.getItemId() == R.id.menu_leaderboard) {
            swapFragment(new LeaderboardFragment());
        } else if (item.getItemId() == R.id.menu_profile) {
            swapFragment(new ProfileFragment());
        }
        return true;
    }

    private boolean active = false;
    private final boolean isActive() {
        return active;
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }
}
