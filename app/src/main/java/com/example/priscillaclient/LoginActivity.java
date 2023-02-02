package com.example.priscillaclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.example.priscillaclient.misc.Preferences;
import com.example.priscillaclient.viewmodels.user.TokenViewModel;
import com.example.priscillaclient.viewmodels.user.models.Token;
import com.example.priscillaclient.misc.LoadingDialog;

public class LoginActivity extends ActivityBase {

    public static boolean userLoggedIn = false;
    private LoadingDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (themeId == 2) {
            ImageView loginLogo = findViewById(R.id.loginLogo);
            loginLogo.setImageResource(R.drawable.priscilla_logo_dark);
        }

        EditText inputPassword = findViewById(R.id.inputPassword);
        inputPassword.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                performLogin(null);
                return true;
            }
            return false;
        });

        dialog = new LoadingDialog(this);

        TokenViewModel viewModel = ViewModelProviders.of(this).get(TokenViewModel.class);
        viewModel.getData().observe(this, this::onUpdate);

        // Observe loading state
        viewModel.getLoadingState().observe(this, (isLoading) -> {
            if (!isLoading)
                dialog.dismiss();
            else
                dialog.show();
        });

        // Observe error state
        viewModel.getErrorState().observe(this, this::showError);

        String username = settings.getString(Preferences.PREFS_USERNAME, null);
        String refreshToken = settings.getString(Preferences.PREFS_REFRESH_TOKEN, null);

        if (!userLoggedIn) {
            if (username != null && refreshToken != null) {
                CheckBox rememberUser = findViewById(R.id.rememberUser);
                rememberUser.setChecked(true);
                ((EditText) findViewById(R.id.inputUsername)).setText(username);
                viewModel.fetchData(username, refreshToken, username, "refresh_token");
            }
        }
    }

    public void redirect(View view) {
        Uri uri = Uri.parse("https://priscilla.fitped.eu/register");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void performLogin(View view) {
        String username = ((EditText) findViewById(R.id.inputUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.inputPassword)).getText().toString();

        TokenViewModel viewModel = ViewModelProviders.of(this).get(TokenViewModel.class);
        viewModel.fetchData(username, password, username, "password");
    }

    public void onUpdate(Token token) {
        if (userLoggedIn || token == null)
            return;

        String username = ((EditText) findViewById(R.id.inputUsername)).getText().toString();

        SharedPreferences.Editor editor = settings.edit();
        CheckBox rememberUser = findViewById(R.id.rememberUser);
        if (rememberUser.isChecked()) {
            editor.putString(Preferences.PREFS_USERNAME, username);
            editor.putString(Preferences.PREFS_REFRESH_TOKEN, token.refresh_token);
        } else {
            editor.putString(Preferences.PREFS_USERNAME, null);
            editor.putString(Preferences.PREFS_REFRESH_TOKEN, null);
        }
        editor.apply();

        userLoggedIn = true;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}