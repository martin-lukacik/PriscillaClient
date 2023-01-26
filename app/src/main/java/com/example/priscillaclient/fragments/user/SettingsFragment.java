package com.example.priscillaclient.fragments.user;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.viewmodel.Pair;
import com.example.priscillaclient.R;
import com.example.priscillaclient.viewmodel.user.ProfileViewModel;
import com.example.priscillaclient.viewmodel.user.SettingsViewModel;
import com.example.priscillaclient.viewmodel.user.UserViewModel;
import com.example.priscillaclient.viewmodel.user.models.Profile;
import com.example.priscillaclient.viewmodel.user.models.Settings;

import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends FragmentBase {

    Profile profile;
    Settings settings;

    EditText profileEditName;
    EditText profileEditSurname;
    EditText profileEditNickname;

    Spinner profileEditGroup;
    Spinner profileEditStudentType;
    Spinner profileEditCountry;
    Spinner profileEditLanguage;
    Spinner profileEditTheme;

    DatePicker profileEditYear;

    TextView settingsSave;

    public SettingsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_settings;

        SettingsViewModel settingsViewModel = (SettingsViewModel) getViewModel(SettingsViewModel.class);
        settingsViewModel.getData().observe(this, this::onUpdateSettings);
        settingsViewModel.fetchData();

        ProfileViewModel profileViewModel = (ProfileViewModel) getViewModel(ProfileViewModel.class);
        profileViewModel.getData().observe(this, this::onUpdateProfile);
        profileViewModel.fetchData();
    }

    private void onUpdateSettings(Settings settings) {
        this.settings = settings;
    }

    private void onUpdateProfile(Profile profile) {

        this.profile = profile;

        profileEditName.setText(profile.name);
        profileEditSurname.setText(profile.surname);
        profileEditNickname.setText(profile.nickname);

        profileEditYear.updateDate(profile.yob, 0, 1);

        loadProfileStudentType(profile);
        loadProfileGroup(settings);
        loadProfileCountry(settings);
        loadProfileLanguage(settings);
        loadProfileTheme(settings);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileEditName = findViewById(R.id.profileEditName);
        profileEditSurname = findViewById(R.id.profileEditSurname);
        profileEditNickname = findViewById(R.id.profileEditNickname);
        profileEditGroup = findViewById(R.id.profileEditGroup);
        profileEditStudentType = findViewById(R.id.profileEditStudentType);
        profileEditCountry = findViewById(R.id.profileEditCountry);
        profileEditLanguage = findViewById(R.id.profileEditLanguage);
        profileEditTheme = findViewById(R.id.profileEditTheme);
        profileEditYear = findViewById(R.id.profileEditYear);

        settingsSave = findViewById(R.id.settingsSaveButton);
        settingsSave.setOnClickListener(this::save);

        profileEditGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setTag(settings.groups.get(i).group_name);
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
                adapterView.setTag(settings.countries.get(i).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        profileEditLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setTag(settings.languages.get(i).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        profileEditTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setTag(settings.themes.get(i).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
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

        int mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        if (theme_id.equals("1")) {
            mode = AppCompatDelegate.MODE_NIGHT_NO;
        } else if (theme_id.equals("2")) {
            mode = AppCompatDelegate.MODE_NIGHT_YES;
        }
        AppCompatDelegate.setDefaultNightMode(mode);

        UserViewModel userViewModel = (UserViewModel) getViewModel(UserViewModel.class);
        userViewModel.update(age, content_type_id, country, group, lang, name, nick, surname, theme_id);

        navigate(R.id.profileFragment, null);
    }

    private void loadSelection(Spinner spinner, String[] items, int selectedIndex) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedIndex);
    }

    private void loadProfileStudentType(Profile profile) {
        String[] items = new String[] { "teen student", "university student" };
        loadSelection(profileEditStudentType, items, profile.content_type_id - 1);
    }

    private void loadProfileGroup(Settings data) {
        Pair<Integer, String[]> selection = data.getGroupSelection(profile);
        loadSelection(profileEditGroup, selection.y, selection.x);
    }

    private void loadProfileCountry(Settings data) {
        Pair<Integer, String[]> selection = data.getCountrySelection(profile);
        loadSelection(profileEditCountry, selection.y, selection.x);
    }

    private void loadProfileLanguage(Settings data) {
        Pair<Integer, String[]> selection = data.getLanguageSelection(profile);
        loadSelection(profileEditLanguage, selection.y, selection.x);
    }

    private void loadProfileTheme(Settings data) {
        Pair<Integer, String[]> selection = data.getThemeSelection(profile);
        loadSelection(profileEditTheme, selection.y, selection.x);
    }

}