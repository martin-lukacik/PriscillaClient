package com.example.priscillaclient;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.priscillaclient.viewmodels.user.models.Theme;

import java.util.Locale;

public class ActivityBase extends AppCompatActivity {

    protected SharedPreferences settings;
    protected int themeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        settings = getSharedPreferences("settings", 0);

        themeId = settings.getInt("theme_id", 0);
        setDarkMode(themeId, false);

        changeLocale(settings.getString("language_shortcut", "en"));

        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.INTERNET }, 1);
        }
    }

    protected void setDarkMode(int themeId, boolean save) {
        if (save) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("theme_id", themeId);
            editor.apply();
        }

        switch (themeId) {
            case Theme.THEME_DARK:
                themeId = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case Theme.THEME_FOLLOW_SYSTEM:
                themeId = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
            default:
                themeId = AppCompatDelegate.MODE_NIGHT_NO;
        }

        if (AppCompatDelegate.getDefaultNightMode() != themeId)
            AppCompatDelegate.setDefaultNightMode(themeId);
    }

    public void changeLocale(String lang, Class className) {
        changeLocale(lang);

        startActivity(new Intent(this, className));
        finish();
    }

    public void changeLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
