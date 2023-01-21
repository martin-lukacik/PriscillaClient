package com.example.priscillaclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

abstract class ActivityBase extends AppCompatActivity {

    protected boolean onMenuItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_dashboard:
                intent = new Intent(ActivityBase.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.menu_all_courses:
                intent = new Intent(ActivityBase.this, CategoryActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.menu_leaderboard:
                intent = new Intent(ActivityBase.this, LeaderboardActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.menu_profile:
                SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("username", null);
                editor.putString("refresh_token", null);
                editor.apply();

                intent = new Intent(ActivityBase.this, LoginActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
