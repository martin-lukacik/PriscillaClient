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

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        User user = client.user;

        if (user != null)
            setActionBarTitle(user.performance.xp + " XP | " + user.performance.coins + " ©");

        new GetUserParams(this).execute();

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView, navController);
    }

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
}