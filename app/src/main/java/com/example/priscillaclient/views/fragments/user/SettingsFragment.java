package com.example.priscillaclient.views.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.priscillaclient.models.Pair;
import com.example.priscillaclient.R;
import com.example.priscillaclient.api.user.ChangeProfile;
import com.example.priscillaclient.api.user.GetProfileData;
import com.example.priscillaclient.api.user.GetRegistrationData;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.models.Profile;
import com.example.priscillaclient.models.RegistrationData;
import com.example.priscillaclient.views.fragments.FragmentBase;

public class SettingsFragment extends FragmentBase {

    Client client = Client.getInstance();

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

        new GetRegistrationData(this).execute();
        new GetProfileData(this).execute();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
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

    @Override
    public void onResume() {
        super.onResume();

        Profile profile = client.profile;
        RegistrationData data = client.registrationData;
        if (profile != null && !data.isEmpty()) {
            onUpdate(profile);
        }
    }

    @Override
    public void onUpdate(Object response) {

        if (response == null) {
            ScrollView scrollView = findViewById(R.id.settingsScrollView);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            client.user = null;
            navigate(new ProfileFragment());
        } else if (response.equals(client.profile)) {
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
        }
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

}