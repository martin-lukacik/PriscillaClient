package com.example.priscillaclient;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.priscillaclient.api.auth.GetUserParams;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.views.fragments.CoursesFragment;
import com.example.priscillaclient.models.User;
import com.example.priscillaclient.views.adapters.CourseListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }

        swapFragment(new CoursesFragment());

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnItemSelectedListener(this::onMenuItemSelected);

        new GetUserParams(this).execute();
    }

    private boolean onMenuItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.my_profile:

                intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

                return true;
            case R.id.leaderboard:

                intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                startActivity(intent);

                return true;
            case R.id.all_courses:
                intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:

                SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("username", null);
                editor.putString("refresh_token", null);
                editor.apply();

                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void swapFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragmentContainerView, fragment)
                //.addToBackStack(null)
                .commit();
    }

    CourseListAdapter adapter;

    @Override
    public void onUpdate(Object response) {

        super.onUpdate(response);

        if (response instanceof User) {
            super.setMenuTitle((User) response);
        }
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            // TODO toto je hack - ak sa vrati user do aplikacie, hodi ho na login (prvu aktivitu)
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);

            // TOTO nie je hack
            //super.onBackPressed();
        }
    }
}