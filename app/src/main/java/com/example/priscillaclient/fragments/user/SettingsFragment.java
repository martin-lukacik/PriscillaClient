package com.example.priscillaclient.fragments.user;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.misc.LoadingDialog;
import com.example.priscillaclient.misc.Pair;
import com.example.priscillaclient.misc.Preferences;
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

public class SettingsFragment extends FragmentBase {

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
    Spinner profileEditMotive;
    Spinner profileEditYear;

    TextView settingsSave;

    LoadingDialog dialog;

    Bundle state;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_settings;

        dialog = new LoadingDialog(getActivity());

        this.settings = (getViewModel(SettingsViewModel.class)).getData().getValue();

        state = savedInstanceState;
        profileViewModel = getViewModel(ProfileViewModel.class);
        profileViewModel.getData().observe(this, this::onUpdate);
        profileViewModel.getErrorState().observe(this, this::showError);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle state) {
        super.onViewCreated(view, state);

        View v = findViewById(R.id.loadingView);
        v.setVisibility(View.GONE);

        profileEditName = findViewById(R.id.profileEditName);
        profileEditSurname = findViewById(R.id.profileEditSurname);
        profileEditNickname = findViewById(R.id.profileEditNickname);
        profileEditGroup = findViewById(R.id.profileEditGroup);
        profileEditStudentType = findViewById(R.id.profileEditStudentType);
        profileEditCountry = findViewById(R.id.profileEditCountry);
        profileEditLanguage = findViewById(R.id.profileEditLanguage);
        profileEditTheme = findViewById(R.id.profileEditTheme);
        profileEditMotive = findViewById(R.id.profileEditMotive);
        profileEditYear = findViewById(R.id.profileEditYear);

        settingsSave = findViewById(R.id.settingsSaveButton);
        settingsSave.setOnClickListener(this::save);
    }

    public void onUpdate(Profile profile) {
        if (profile == null)
            return;

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
        loadProfileMotive();

        if (state != null) {
            profileEditYear.setSelection(state.getInt("year"));
            profileEditStudentType.setSelection(state.getInt("student_type"));
            profileEditCountry.setSelection(state.getInt("country"));
            profileEditGroup.setSelection(state.getInt("group"));
            profileEditLanguage.setSelection(state.getInt("lang"));
            profileEditTheme.setSelection(state.getInt("theme"));
            profileEditMotive.setSelection(state.getInt("motive"));
            profileEditName.setText(state.getString("name"));
            profileEditNickname.setText(state.getString("nick"));
            profileEditSurname.setText(state.getString("surname"));
            state = null;
        }
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
        outState.putInt("motive", profileEditMotive.getSelectedItemPosition());
        outState.putString("name", profileEditName.getText().toString());
        outState.putString("nick", profileEditNickname.getText().toString());
        outState.putString("surname", profileEditSurname.getText().toString());
    }

    boolean isThemeChanged = false;
    int changedThemeId = 1;

    boolean isMotiveChanged = false;
    int changedMotive = -1;

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
        int motive = profileEditMotive.getSelectedItemPosition();
        String shortcut = settings.languages.get(profileEditLanguage.getSelectedItemPosition()).shortcut.toLowerCase();

        if (preferences.getInt(Preferences.PREFS_THEME_ID, 1) != theme_id) {
            isThemeChanged = true;
            changedThemeId = theme_id;
        }

        int savedMotive = preferences.getInt(Preferences.PREFS_MOTIVE, -1);
        if (savedMotive != motive) {
            isMotiveChanged = true;
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
            || isMotiveChanged
        ) {
            UserViewModel userViewModel = getViewModel(UserViewModel.class);
            userViewModel.update(age, content_type_id, country, group, lang, name, nick, surname, theme_id);

            dialog.show();
            Observer<User> observer = new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user == null)
                        return;

                    dialog.dismiss();

                    profileViewModel.fetchData();

                    userViewModel.getData().removeObserver(this);

                    String savedShortcut = preferences.getString(Preferences.PREFS_LANGUAGE_SHORTCUT, "en");
                    if (!savedShortcut.equals(shortcut)) {

                        clearLocalizedViewModels();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Preferences.PREFS_LANGUAGE_SHORTCUT, shortcut);
                        editor.apply();

                        ((ActivityBase) requireActivity()).changeLocale(shortcut, false);
                    }

                    boolean needsRecreate = false;
                    if (isThemeChanged) {
                        needsRecreate = true;
                        ((ActivityBase) requireActivity()).setDarkMode(changedThemeId, true, false);
                    }
                    if (isMotiveChanged) {
                        needsRecreate = true;

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(Preferences.PREFS_MOTIVE, motive);
                        editor.apply();
                    }

                    if (needsRecreate) {
                        requireActivity().recreate();
                    }

                    requireActivity().onBackPressed(); // navigate back to profile
                }
            };

            userViewModel.getData().observe(this, observer);
        } else {
            String msg = getResources().getString(R.string.settings_no_changes);
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    private void clearLocalizedViewModels() {
        CoursesViewModel coursesViewModel = getViewModel(CoursesViewModel.class);
        coursesViewModel.clear();

        ChaptersViewModel chaptersViewModel = getViewModel(ChaptersViewModel.class);
        chaptersViewModel.clear();

        LessonsViewModel lessonsViewModel = getViewModel(LessonsViewModel.class);
        lessonsViewModel.clear();

        TasksViewModel tasksViewModel = getViewModel(TasksViewModel.class);
        tasksViewModel.clear();

        CategoriesViewModel categoriesViewModel = getViewModel(CategoriesViewModel.class);
        categoriesViewModel.clear();

        AreasViewModel areasViewModel = getViewModel(AreasViewModel.class);
        areasViewModel.clear();

        AreaCoursesViewModel areaCoursesViewModel = getViewModel(AreaCoursesViewModel.class);
        areaCoursesViewModel.clear();
    }

    private void loadSelection(Spinner spinner, String[] items, int selectedIndex) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedIndex);
    }

    private void loadProfileMotive() {
        int savedIndex = preferences.getInt(Preferences.PREFS_MOTIVE, 0);
        String[] items = new String[] { "Random", "Purple", "Blue", "Green", "Orange", "Red" };
        int[] colors = new int[] { // TODO hardcoded
            0,
            Color.parseColor("#3700B3"),
            Color.parseColor("#004f99"),
            Color.parseColor("#017020"),
            Color.parseColor("#955001"),
            Color.parseColor("#8c1b1a"),
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_row, items) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                int id = getContext().getResources().getIdentifier("ic_rectangle", "drawable", getContext().getPackageName());
                TextView textView = v.findViewById(R.id.spinnerLanguage);
                Drawable drawable = ContextCompat.getDrawable(getContext(), id);


                if (position > 0) {
                    drawable.setColorFilter(colors[position], PorterDuff.Mode.SRC_ATOP);


                    textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
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
        profileEditMotive.setAdapter(adapter);
        profileEditMotive.setSelection(savedIndex);
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

                SettingsViewModel settingsViewModel = getViewModel(SettingsViewModel.class);

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