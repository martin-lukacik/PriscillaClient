package com.example.priscillaclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.priscillaclient.util.Preferences;
import com.example.priscillaclient.viewmodels.user.ProfileViewModel;
import com.example.priscillaclient.viewmodels.user.SettingsViewModel;
import com.example.priscillaclient.viewmodels.user.UserViewModel;
import com.example.priscillaclient.viewmodels.user.models.Language;
import com.example.priscillaclient.viewmodels.user.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
 * TODO Loading should show dialogs and/or clear previous state (Courses -> Chapters -> Tasks shows old data while loading)
 * TODO Colorblind mode?
 * TODO Submit task resets inputs
 * TODO Proper multi-language support based on app setting
 * TODO ViewPager for task navigation
 */
/*help 10
answer 20

code 20, 40

https://app.priscilla.fitped.eu/get-my-help/{TASK_ID}
help = String


/get-my-answer/{TASK_ID}
answers = [] { answer, feedback }

answers = {"title":"House (Dom)","assignment":[""],"files":[{"rContent":"public class House {\n\n}","aContent":"public class House {\n    String color;\n    double height;\n    double width = 10.5;\n    int rooms = 5;\n}"},{"rContent":"public class MainApp {\n  \n    public static void main() {\n        \/\/ toto je len príklad použitia, tento kód sa nevykonáva\n        House example = new House(); \/\/ vytvori instanciu\n        example.color = \"green\";\n        example.height = 6.2;\n    \texample.width = 12.5;\n    \texample.rooms = 8;\n    }\n}","aContent":""}],"testCases":"","filesToKeep":[""],"configFiles":[""],"help":"","global":{"files":{"files":["House.java","MainApp.java"],"filesToKeep":["file1.txt"],"configFiles":["vpl_run.sh","vpl_debug.sh","vpl_evaluate.sh","Main.java","Evaluate.java","MySolution.java"]}}}
*/

public class MainActivity extends ActivityBase {

    public static final String INTENT_COURSE_ID = "course_id";

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int courseId = getIntent().getIntExtra(INTENT_COURSE_ID, -1);

        if (courseId != -1) {
            Bundle args = new Bundle();
            args.putInt(INTENT_COURSE_ID, courseId);
            navigate(R.id.chaptersFragment, args);
        }

        setupNavigation();

        fetchData();
    }

    private void fetchData() {
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getData().observe(this, (data) -> {
            if (userViewModel.hasError())
                showError(userViewModel.getError());
            else
                onUpdate(data);
        });
        userViewModel.fetchData();


        // TODO synchronize with registration data
        ProfileViewModel profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.fetchData();

        SettingsViewModel settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsViewModel.fetchData();
        settingsViewModel.getData().observe(this, (data) -> {
            profileViewModel.getData().observe(this, (d) -> {

                if (d == null)
                    return;

                String currentShortcut = settings.getString(Preferences.PREFS_LANGUAGE_SHORTCUT, "en");
                String shortcut = "en";

                for (Language language : data.languages) {
                    if (language.id == d.pref_lang_id) {
                        shortcut = language.shortcut;
                        break;
                    }
                }

                if (!shortcut.equals(currentShortcut)) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Preferences.PREFS_LANGUAGE_SHORTCUT, shortcut);
                    editor.apply();
                    // TODO restart activity to take effect
                    changeLocale(shortcut);
                }
            });
        });
    }

    private void setupNavigation() {
        FragmentManager manager = getSupportFragmentManager();
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigation);
        NavHostFragment navHostFragment = (NavHostFragment) manager.findFragmentById(R.id.fragmentContainer);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(navigationView, navController);
        }
    }

    @Override
    public boolean onNavigateUp() {
        return navController.navigateUp() || super.onNavigateUp();
    }

    public void onUpdate(User user) {
        if (user == null)
            return;

        setActionBarTitle(user.performance.xp + " XP | " + user.performance.coins + " ©");
        setDarkMode(user.theme_id, true);
    }

    void setActionBarTitle(String title) {
        TextView tv = findViewById(R.id.actionBarTitle);
        tv.setText(title);
    }

    public void navigate(int layoutId, Bundle args) {
        navController.navigate(layoutId, args);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int courseId = intent.getIntExtra("course_id", -1);

        if (courseId != -1) {
            Bundle args = new Bundle();
            args.putInt("courseId", courseId);
            navigate(R.id.chaptersFragment, args);
        }
    }

    @Override
    public void onBackPressed(){
        FragmentManager manager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) manager.findFragmentById(R.id.fragmentContainer);

        if (navHostFragment != null) {
            FragmentManager fm = navHostFragment.getChildFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                super.onBackPressed();
            } else {
                // Prevents login screen on return to the app
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
            }
        }
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