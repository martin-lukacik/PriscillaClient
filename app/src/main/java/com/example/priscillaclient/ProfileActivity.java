package com.example.priscillaclient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.user.GetProfileData;
import com.example.priscillaclient.api.user.GetRegistrationData;
import com.example.priscillaclient.api.user.GetUserParams;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.models.Profile;
import com.example.priscillaclient.models.RegistrationData;
import com.example.priscillaclient.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class ProfileActivity extends ActivityBase implements HttpResponse {

    EditText profileEditName;
    EditText profileEditSurname;

    Spinner profileEditGroup;
    Spinner profileEditStudentType;
    Spinner profileEditCountry;
    Spinner profileEditLanguage;
    Spinner profileEditTheme;

    DatePicker profileEditYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileEditName = findViewById(R.id.profileEditName);
        profileEditSurname = findViewById(R.id.profileEditSurname);
        profileEditGroup = findViewById(R.id.profileEditGroup);
        profileEditStudentType = findViewById(R.id.profileEditStudentType);
        profileEditCountry = findViewById(R.id.profileEditCountry);
        profileEditLanguage = findViewById(R.id.profileEditLanguage);
        profileEditTheme = findViewById(R.id.profileEditTheme);
        profileEditYear = findViewById(R.id.profileEditYear);

        new GetUserParams(this).execute();

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(this::onMenuItemSelected);
        navigationView.getMenu().findItem(R.id.menu_profile).setChecked(true);

        new GetRegistrationData(this).execute();
        new GetProfileData(this).execute();
    }

    private void loadProfileStudentType() {
        String[] items = new String[] { "university student", "teen student" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        profileEditStudentType.setAdapter(adapter);
        profileEditStudentType.setSelection(0);
    }

    private void loadProfileGroup() {
        Client client = Client.getInstance();
        Profile profile = client.profile;
        RegistrationData data = client.registrationData;
        int selectedIndex = -1;
        String[] items = new String[data.groups.size()];
        for (int i = 0; i < data.groups.size(); ++i) {
            items[i] = data.groups.get(i).group_name;

            if (data.groups.get(i).group_name.equals(profile.groups)) {
                selectedIndex = i;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        profileEditGroup.setAdapter(adapter);
        profileEditGroup.setSelection(selectedIndex);
    }

    private void loadProfileCountry() {
        Client client = Client.getInstance();
        Profile profile = client.profile;
        RegistrationData data = client.registrationData;
        int selectedIndex = -1;
        String[] items = new String[data.countries.size()];
        for (int i = 0; i < data.countries.size(); ++i) {
            items[i] = data.countries.get(i).country_name;

            if (data.countries.get(i).id == profile.country_id) {
                selectedIndex = i;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        profileEditCountry.setAdapter(adapter);
        profileEditCountry.setSelection(selectedIndex);
    }

    private void loadProfileLanguage() {
        Client client = Client.getInstance();
        Profile profile = client.profile;
        RegistrationData data = client.registrationData;
        int selectedIndex = -1;
        String[] items = new String[data.languages.size()];
        for (int i = 0; i < data.languages.size(); ++i) {
            items[i] = data.languages.get(i).name;

            if (data.languages.get(i).id == profile.pref_lang_id) {
                selectedIndex = i;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        profileEditLanguage.setAdapter(adapter);
        profileEditLanguage.setSelection(selectedIndex);
    }

    private void loadProfileTheme() {
        Client client = Client.getInstance();
        Profile profile = client.profile;
        RegistrationData data = client.registrationData;
        int selectedIndex = -1;
        String[] items = new String[data.themes.size()];
        for (int i = 0; i < data.themes.size(); ++i) {
            items[i] = data.themes.get(i).theme_name;

            if (data.themes.get(i).id == profile.theme_id) {
                selectedIndex = i;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        profileEditTheme.setAdapter(adapter);
        profileEditTheme.setSelection(selectedIndex);
    }

    @Override
    public void onUpdate(Object response) {

        Client client = Client.getInstance();

        if (response.equals(client.registrationData)) {

            return;
        }

        if (response.equals(client.profile)) {

            Profile profile = client.profile;

            profileEditName.setText(profile.name);
            profileEditSurname.setText(profile.surname);

            profileEditYear.updateDate(profile.yob, 0, 1);

            loadProfileGroup();
            loadProfileStudentType();
            loadProfileCountry();
            loadProfileLanguage();
            loadProfileTheme();

            return;
        }

        TextView usernameShort = findViewById(R.id.usernameShort);
        TextView usernameFull = findViewById(R.id.usernameFull);

        User user = Client.getInstance().user;

        usernameShort.setText(user.name.charAt(0) + "" + user.surname.charAt(0));
        usernameFull.setText(user.name + " " + user.surname);

        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        int color = Color.argb(255, r, g, b);

        GradientDrawable drawable = (GradientDrawable) usernameShort.getBackground();
        drawable.mutate();
        drawable.setStroke(5, color);
        usernameShort.setTextColor(color);


        TextView profileLevel = findViewById(R.id.profileLevel);
        TextView profileXP = findViewById(R.id.profileXP);
        TextView profileCoins = findViewById(R.id.profileCoins);
        TextView profileEmail = findViewById(R.id.profileEmail);

        profileLevel.setText(user.performance.level + "");
        profileXP.setText(user.performance.xp + "");
        profileCoins.setText(user.performance.coins + "");
        profileEmail.setText(user.email);

        Activity _this = this;
        profileEditGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            boolean skip = true;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (skip) {
                    skip = false;
                    return;
                }

                Toast.makeText(_this, client.registrationData.groups.get(i).group_name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    public void logout(View view) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", null);
        editor.putString("refresh_token", null);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}