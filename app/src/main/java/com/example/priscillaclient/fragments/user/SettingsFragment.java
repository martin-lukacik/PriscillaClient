package com.example.priscillaclient.fragments.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.example.priscillaclient.ActivityBase;
import com.example.priscillaclient.R;
import com.example.priscillaclient.fragments.FragmentAdapter;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.util.Pair;
import com.example.priscillaclient.util.Preferences;
import com.example.priscillaclient.viewmodels.app.ChaptersViewModel;
import com.example.priscillaclient.viewmodels.app.CoursesViewModel;
import com.example.priscillaclient.viewmodels.app.LessonsViewModel;
import com.example.priscillaclient.viewmodels.app.TasksViewModel;
import com.example.priscillaclient.viewmodels.browse.AreaCoursesViewModel;
import com.example.priscillaclient.viewmodels.browse.AreasViewModel;
import com.example.priscillaclient.viewmodels.browse.CategoriesViewModel;
import com.example.priscillaclient.viewmodels.user.ProfileViewModel;
import com.example.priscillaclient.viewmodels.user.SettingsViewModel;
import com.example.priscillaclient.viewmodels.user.UserViewModel;
import com.example.priscillaclient.viewmodels.user.models.Profile;
import com.example.priscillaclient.viewmodels.user.models.Settings;
import com.example.priscillaclient.viewmodels.user.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class SettingsFragment extends FragmentBase implements FragmentAdapter<Profile> {

    ProfileViewModel profileViewModel;

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
    Spinner profileEditYear;

    TextView settingsSave;

    public SettingsFragment() { }

    Bundle state;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_settings;

        this.settings = ((SettingsViewModel) getViewModel(SettingsViewModel.class)).getData().getValue();

        state = savedInstanceState;
        profileViewModel = getViewModel(ProfileViewModel.class);
        profileViewModel.getData().observe(this, onResponse(profileViewModel.getError()));
    }

    public void onUpdate(Profile profile) {

        this.profile = profile;

        profileEditName.setText(profile.name);
        profileEditSurname.setText(profile.surname);
        profileEditNickname.setText(profile.nickname);

        loadProfileYear(profile);
        loadProfileStudentType(profile);
        loadProfileGroup(settings);
        loadProfileCountry(settings);
        loadProfileLanguage(settings);
        loadProfileTheme(settings);

        if (state != null) {
            profileEditYear.setSelection(state.getInt("year"));
            profileEditStudentType.setSelection(state.getInt("student_type"));
            profileEditCountry.setSelection(state.getInt("country"));
            profileEditGroup.setSelection(state.getInt("group"));
            profileEditLanguage.setSelection(state.getInt("lang"));
            profileEditTheme.setSelection(state.getInt("theme"));
            profileEditName.setText(state.getString("name"));
            profileEditNickname.setText(state.getString("nick"));
            profileEditSurname.setText(state.getString("surname"));
            state = null;
        }
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle state) {
        super.onViewCreated(view, state);

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
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("year", profileEditYear.getSelectedItemPosition());
        outState.putInt("student_type", profileEditStudentType.getSelectedItemPosition());
        outState.putInt("country", profileEditCountry.getSelectedItemPosition());
        outState.putInt("group", profileEditGroup.getSelectedItemPosition());
        outState.putInt("lang", profileEditLanguage.getSelectedItemPosition());
        outState.putInt("theme", profileEditTheme.getSelectedItemPosition());
        outState.putString("name", profileEditName.getText().toString());
        outState.putString("nick", profileEditNickname.getText().toString());
        outState.putString("surname", profileEditSurname.getText().toString());
    }

    public void showLanguageChangeDialog(String shortcut) {
        if (!isThemeChanged) {
            ((ActivityBase) requireActivity()).changeLocale(shortcut, true);
        } else {
            ((ActivityBase) requireActivity()).changeLocale(shortcut, true);
            ((ActivityBase) requireActivity()).setDarkMode(changedThemeId, true);
        }
    }

    boolean isThemeChanged = false;
    int changedThemeId = 1;
    public void save(View view) {
        int age = profileEditYear.getSelectedItemPosition() + startingYear;
        int content_type_id = profileEditStudentType.getSelectedItemPosition() + 1;
        int country = settings.countries.get(profileEditCountry.getSelectedItemPosition()).id;
        String group = settings.groups.get(profileEditGroup.getSelectedItemPosition()).group_name;
        int lang = settings.languages.get(profileEditLanguage.getSelectedItemPosition()).id;
        String name = profileEditName.getText().toString();
        String nick = profileEditNickname.getText().toString();
        String surname = profileEditSurname.getText().toString();
        int theme_id = settings.themes.get(profileEditTheme.getSelectedItemPosition()).id;
        String shortcut = settings.languages.get(profileEditLanguage.getSelectedItemPosition()).shortcut.toLowerCase();

        SharedPreferences settings = requireActivity().getSharedPreferences(Preferences.PREFS, 0);

        if (settings.getInt(Preferences.PREFS_THEME_ID, 1) != theme_id) {
            isThemeChanged = true;
            changedThemeId = theme_id;
        }

        if (profile.yob != age
            || profile.content_type_id != content_type_id
            || profile.country_id != country
            || !profile.groups.equals(group)
            || profile.pref_lang_id != lang
            || !profile.name.equals(name)
            || !profile.nickname.equals(nick)
            || !profile.surname.equals(surname)
            || profile.theme_id != theme_id
        ) {
            UserViewModel userViewModel = (UserViewModel) getViewModel(UserViewModel.class);
            userViewModel.update(age, content_type_id, country, group, lang, name, nick, surname, theme_id);

            Observer<User> observer = new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user == null)
                        return;
                    profileViewModel.fetchData();
                    userViewModel.getData().removeObserver(this);
                }
            };
            userViewModel.getData().observe(this, observer);


            String savedShortcut = settings.getString(Preferences.PREFS_LANGUAGE_SHORTCUT, "en");
            if (!savedShortcut.equals(shortcut)) {

                clearLocalizedViewModels();

                SharedPreferences.Editor editor = settings.edit();
                editor.putString(Preferences.PREFS_LANGUAGE_SHORTCUT, shortcut);
                editor.apply();

                showLanguageChangeDialog(shortcut);
            } else if (isThemeChanged) {
                ((ActivityBase) requireActivity()).setDarkMode(theme_id, true);
            }
        } else {
            String msg = getResources().getString(R.string.settings_no_changes);
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    private void clearLocalizedViewModels() {
        CoursesViewModel coursesViewModel = (CoursesViewModel) getViewModel(CoursesViewModel.class);
        coursesViewModel.clear();

        ChaptersViewModel chaptersViewModel = (ChaptersViewModel) getViewModel(ChaptersViewModel.class);
        chaptersViewModel.clear();

        LessonsViewModel lessonsViewModel = (LessonsViewModel) getViewModel(LessonsViewModel.class);
        lessonsViewModel.clear();

        TasksViewModel tasksViewModel = (TasksViewModel) getViewModel(TasksViewModel.class);
        tasksViewModel.clear();

        CategoriesViewModel categoriesViewModel = (CategoriesViewModel) getViewModel(CategoriesViewModel.class);
        categoriesViewModel.clear();

        AreasViewModel areasViewModel = (AreasViewModel) getViewModel(AreasViewModel.class);
        areasViewModel.clear();

        AreaCoursesViewModel areaCoursesViewModel = (AreaCoursesViewModel) getViewModel(AreaCoursesViewModel.class);
        areaCoursesViewModel.clear();
    }

    private void loadSelection(Spinner spinner, String[] items, int selectedIndex) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedIndex);
    }

    private void loadProfileStudentType(Profile profile) {
        String[] items = new String[] { "teen student", "university student" }; // TODO hardcored
        loadSelection(profileEditStudentType, items, profile.content_type_id - 1);
    }

    private final static int startingYear = 1940;
    private void loadProfileYear(Profile profile) {
        int year = Calendar.getInstance().get(Calendar.YEAR);

        String[] items = new String[year - startingYear + 1];
        for (int i = startingYear; i <= year; ++i) {
            items[i - startingYear] = i + "";
        }

        loadSelection(profileEditYear, items, profile.yob - startingYear);
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_row, R.id.spinnerLanguage, selection.y) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                SettingsViewModel settingsViewModel = (SettingsViewModel) getViewModel(SettingsViewModel.class);

                if (settingsViewModel.getData().getValue() != null) {
                    String shortcut = settingsViewModel.getData().getValue().languages.get(position).shortcut;
                    int id = getContext().getResources().getIdentifier("flag_" + shortcut.toLowerCase(), "drawable", getContext().getPackageName());
                    TextView textView = v.findViewById(R.id.spinnerLanguage);
                    textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), id), null, null, null);
                }
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
                View v = getView(position, convertView, parent);
                v.setPadding(0, 10, 0, 10);
                return v;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_row);
        profileEditLanguage.setAdapter(adapter);
        profileEditLanguage.setSelection(selection.x);
    }

    private void loadProfileTheme(Settings data) {
        Pair<Integer, String[]> selection = data.getThemeSelection(profile);
        loadSelection(profileEditTheme, selection.y, selection.x);
    }
}