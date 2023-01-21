package com.example.priscillaclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.user.GetToken;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.views.LoadingDialog;

public class LoginActivity extends AppCompatActivity implements HttpResponse {

    LoadingDialog loadingDialog;

    String refresh_token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText inputUsername = findViewById(R.id.inputUsername);
        inputUsername.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                performLogin(null);
                return true;
            }
            return false;
        });
        EditText inputPassword = findViewById(R.id.inputPassword);
        inputPassword.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                performLogin(null);
                return true;
            }
            return false;
        });

        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
        String username = settings.getString("username", null);
        refresh_token = settings.getString("refresh_token", null);
        if (username != null && refresh_token != null) {
            CheckBox rememberUser = findViewById(R.id.rememberUser);
            rememberUser.setChecked(true);
            ((EditText) findViewById(R.id.inputUsername)).setText(username);
            showProgressDialog();
            apiCall = new GetToken(this);
            apiCall.execute(username, refresh_token, username, "refresh_token");
        }
    }

    public void redirect(View view) {
        Uri uri = Uri.parse("https://priscilla.fitped.eu/register");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void showProgressDialog() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );

        loadingDialog = new LoadingDialog(LoginActivity.this, "Logging in...");
        loadingDialog.show();
    }

    public void performLogin(View view) {
        String username = ((EditText) findViewById(R.id.inputUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.inputPassword)).getText().toString();

        showProgressDialog();

        apiCall = new GetToken(this);
        apiCall.execute(username, password, username, "password");
    }

    GetToken apiCall;

    @Override
    public void onUpdate(Object response) {

        loadingDialog.dismiss();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (apiCall != null && apiCall.errorMessage != null) {
            return;
        }

        String username = ((EditText) findViewById(R.id.inputUsername)).getText().toString();

        // TODO check if actually logged in (wrong password etc.)
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        CheckBox rememberUser = findViewById(R.id.rememberUser);
        if (rememberUser.isChecked()) {
            editor.putString("username", username);
            editor.putString("refresh_token", Client.getInstance().refresh_token);
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