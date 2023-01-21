package com.example.priscillaclient;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

abstract class ActivityBase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("");
        getSupportActionBar().setIcon(R.drawable.priscilla_logo_dark);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xfff9f9f9));*/
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    protected boolean onMenuItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_dashboard:
                intent = new Intent(ActivityBase.this, MainActivity.class);
                break;

            case R.id.menu_all_courses:
                intent = new Intent(ActivityBase.this, CategoryActivity.class);
                break;

            case R.id.menu_leaderboard:
                intent = new Intent(ActivityBase.this, LeaderboardActivity.class);
                break;

            case R.id.menu_profile:
                intent = new Intent(ActivityBase.this, ProfileActivity.class);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        startActivity(intent);
        overridePendingTransition(0, 0);
        return true;
    }
}
