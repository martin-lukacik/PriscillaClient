package com.example.priscillaclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.priscillaclient.user.viewmodel.TokenViewModel;
import com.example.priscillaclient.user.viewmodel.models.Token;
import com.example.priscillaclient.util.LoadingDialog;

public class LoginActivity extends AppCompatActivity {

    LoadingDialog dialog;
    String refresh_token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText inputUsername = findViewById(R.id.inputUsername);
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
        viewModel.getData().observe(this, (data) -> {
            if (viewModel.hasError()) {
                dialog.dismiss();
                Toast.makeText(this, viewModel.getError(), Toast.LENGTH_LONG).show();
            }
            else
                onUpdate(data);
        });
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
        String username = settings.getString("username", null);
        refresh_token = settings.getString("refresh_token", null);
        if (username != null && refresh_token != null) {
            CheckBox rememberUser = findViewById(R.id.rememberUser);
            rememberUser.setChecked(true);
            ((EditText) findViewById(R.id.inputUsername)).setText(username);
            viewModel.fetchData(username, refresh_token, username, "refresh_token");
            dialog.show();
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

        //new GetTokenLegacy(this).execute(username, password, username, "password");
        TokenViewModel viewModel = ViewModelProviders.of(this).get(TokenViewModel.class);
        viewModel.fetchData(username, password, username, "password");
        dialog.show();
    }

    public void onUpdate(Token token) {

        if (token == null)
            return;

        dialog.dismiss();

        String username = ((EditText) findViewById(R.id.inputUsername)).getText().toString();

        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        CheckBox rememberUser = findViewById(R.id.rememberUser);
        if (rememberUser.isChecked()) {
            editor.putString("username", username);
            editor.putString("refresh_token", token.refresh_token);
        } else {
            editor.putString("username", null);
            editor.putString("refresh_token", null);
        }
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}