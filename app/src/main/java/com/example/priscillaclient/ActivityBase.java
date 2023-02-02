package com.example.priscillaclient;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.priscillaclient.misc.Preferences;
import com.example.priscillaclient.viewmodels.user.models.Theme;

import java.util.Locale;

public class ActivityBase extends AppCompatActivity {

    protected SharedPreferences settings;
    protected int themeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        settings = getSharedPreferences(Preferences.PREFS, 0);

        themeId = settings.getInt(Preferences.PREFS_THEME_ID, 0);
        setDarkMode(themeId, false);

        changeLocale(settings.getString(Preferences.PREFS_LANGUAGE_SHORTCUT, "en"), false);

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

    public void setDarkMode(int themeId, boolean save) {
        if (save) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(Preferences.PREFS_THEME_ID, themeId);
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

    public void changeLocale(String lang, boolean refresh) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        if (refresh) {
            recreate();
        }
    }

    public boolean isDarkModeEnabled() {
        int uiMode = getResources().getConfiguration().uiMode;
        int nightModeFlags = uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    public void showError(String error) {
        if (error != null) {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();

            if (error.equals("Unauthorized.")) {
                LoginActivity.userLoggedIn = false;
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }
    }
}
