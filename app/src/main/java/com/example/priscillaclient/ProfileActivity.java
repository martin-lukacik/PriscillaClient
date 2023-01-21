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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.user.ChangeProfile;
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
    EditText profileEditNickname;

    Spinner profileEditGroup;
    Spinner profileEditStudentType;
    Spinner profileEditCountry;
    Spinner profileEditLanguage;
    Spinner profileEditTheme;

    DatePicker profileEditYear;

    Client client = Client.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileEditName = findViewById(R.id.profileEditName);
        profileEditSurname = findViewById(R.id.profileEditSurname);
        profileEditNickname = findViewById(R.id.profileEditNickname);
        profileEditGroup = findViewById(R.id.profileEditGroup);
        profileEditStudentType = findViewById(R.id.profileEditStudentType);
        profileEditCountry = findViewById(R.id.profileEditCountry);
        profileEditLanguage = findViewById(R.id.profileEditLanguage);
        profileEditTheme = findViewById(R.id.profileEditTheme);
        profileEditYear = findViewById(R.id.profileEditYear);

        profileEditGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setTag(client.registrationData.groups.get(i).group_name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        profileEditStudentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setTag(i + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        profileEditCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setTag(client.registrationData.countries.get(i).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        profileEditLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setTag(client.registrationData.languages.get(i).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        profileEditTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setTag(client.registrationData.themes.get(i).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        new GetUserParams(this).execute();

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(this::onMenuItemSelected);
        navigationView.getMenu().findItem(R.id.menu_profile).setChecked(true);

        new GetRegistrationData(this).execute();
        new GetProfileData(this).execute();
    }

    private void loadSelection(Spinner spinner, String[] items, int selectedIndex) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedIndex);
    }

    private void loadProfileStudentType(Profile profile) {
        String[] items = new String[] { "teen student", "university student" };
        loadSelection(profileEditStudentType, items, profile.content_type_id - 1);
    }

    private void loadProfileGroup(RegistrationData data) {
        Pair<Integer, String[]> selection = data.getGroupSelection();
        loadSelection(profileEditGroup, selection.y, selection.x);
    }

    private void loadProfileCountry(RegistrationData data) {
        Pair<Integer, String[]> selection = data.getCountrySelection();
        loadSelection(profileEditCountry, selection.y, selection.x);
    }

    private void loadProfileLanguage(RegistrationData data) {
        Pair<Integer, String[]> selection = data.getLanguageSelection();
        loadSelection(profileEditLanguage, selection.y, selection.x);
    }

    private void loadProfileTheme(RegistrationData data) {
        Pair<Integer, String[]> selection = data.getThemeSelection();
        loadSelection(profileEditTheme, selection.y, selection.x);
    }

    @Override
    public void onUpdate(Object response) {

        if (response == null) {

            ScrollView scrollView = findViewById(R.id.profileScrollView);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            client.user = null;
            new GetUserParams(this).execute();
            return;
        }

        if (response.equals(client.registrationData)) {

            return;
        }

        if (response.equals(client.profile)) {

            Profile profile = client.profile;
            RegistrationData data = client.registrationData;

            profileEditName.setText(profile.name);
            profileEditSurname.setText(profile.surname);
            profileEditNickname.setText(profile.nickname);

            profileEditYear.updateDate(profile.yob, 0, 1);

            loadProfileStudentType(profile);
            loadProfileGroup(data);
            loadProfileCountry(data);
            loadProfileLanguage(data);
            loadProfileTheme(data);

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

    public void save(View view) {
        String age = profileEditYear.getYear() + "";
        String content_type_id = profileEditStudentType.getTag().toString();
        String country = profileEditCountry.getTag().toString();
        String group = profileEditGroup.getTag().toString();
        String lang = profileEditLanguage.getTag().toString();
        String name = profileEditName.getText().toString();
        String nick = profileEditNickname.getText().toString();
        String surname = profileEditSurname.getText().toString();
        String theme_id = profileEditTheme.getTag().toString();

        new ChangeProfile(this).execute(age, content_type_id, country, group, lang, name, nick, surname, theme_id);
    }
}