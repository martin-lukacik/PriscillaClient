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

public abstract class ActivityBase extends AppCompatActivity implements HttpResponse {

    Client client = Client.getInstance();

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

    protected void swapFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
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

        Intent intent = null;
        if (item.getItemId() == R.id.menu_dashboard /*&& !(this instanceof MainActivity)*/)
            intent = new Intent(ActivityBase.this, MainActivity.class);

        if (item.getItemId() == R.id.menu_all_courses && !(this instanceof CategoryActivity))
            intent = new Intent(ActivityBase.this, CategoryActivity.class);

        if (item.getItemId() == R.id.menu_leaderboard && !(this instanceof LeaderboardActivity))
            intent = new Intent(ActivityBase.this, LeaderboardActivity.class);

        if (item.getItemId() == R.id.menu_profile && !(this instanceof ProfileActivity))
            intent = new Intent(ActivityBase.this, ProfileActivity.class);

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
        return true;
    }
}
