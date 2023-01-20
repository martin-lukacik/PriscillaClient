package com.example.priscillaclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.client.Client;
import com.example.priscillaclient.models.User;

public class BaseActivity extends AppCompatActivity implements HttpResponse {

    Menu menu = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        this.menu = menu;

        if (Client.getInstance().user != null)
            setMenuTitle(Client.getInstance().user);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.my_profile:

                intent = new Intent(BaseActivity.this, ProfileActivity.class);
                startActivity(intent);

                return true;
            case R.id.leaderboard:

                intent = new Intent(BaseActivity.this, LeaderboardActivity.class);
                startActivity(intent);

                return true;
            case R.id.all_courses:
                intent = new Intent(BaseActivity.this, CategoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:

                SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("username", null);
                editor.putString("refresh_token", null);
                editor.apply();

                intent = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setMenuTitle(User user) {
        String fullName = user.name + " " + user.surname;
        menu.add(0, 0, 0, fullName)
                .setEnabled(false);
    }

    @Override
    public void onUpdate(Object response) {
    }
}
