package com.example.priscillaclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

abstract class ActivityBase extends AppCompatActivity {

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
