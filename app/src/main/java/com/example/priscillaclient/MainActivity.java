package com.example.priscillaclient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.models.User;
import com.example.priscillaclient.viewmodel.user.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

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

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView, navController);

        UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        viewModel.getData().observe(this, (data) -> {
            if (viewModel.hasError())
                showError(viewModel.getError());
            else
                onUpdate(data);
        });
        viewModel.fetchData();
    }

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