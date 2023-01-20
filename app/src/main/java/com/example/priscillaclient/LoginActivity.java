package com.example.priscillaclient;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.RequestToken;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.models.User;
import com.example.priscillaclient.views.LoadingDialog;

public class LoginActivity extends AppCompatActivity implements HttpResponse {

    LoadingDialog loadingDialog;

    String refresh_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);

/*
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", null);
        editor.putString("refresh_token", null);
        editor.apply();*/

        String username = settings.getString("username", null);
        refresh_token = settings.getString("refresh_token", null);
        if (username != null && refresh_token != null) {
            ((EditText) findViewById(R.id.inputUsername)).setText(username);
            loadingDialog = new LoadingDialog(LoginActivity.this, "Logging in...");
            loadingDialog.show();
            apiCall = new RequestToken(this);
            apiCall.execute(username, refresh_token, username, "refresh_token");
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

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );


        loadingDialog = new LoadingDialog(LoginActivity.this, "Logging in...");
        loadingDialog.show();
        apiCall = new RequestToken(this);
        apiCall.execute(username, password, username, "password");
    }

    RequestToken apiCall;

    @Override
    public void onUpdate(Object response) {

        loadingDialog.dismiss();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (apiCall != null && apiCall.errorMessage != null)
            return;

        String username = ((EditText) findViewById(R.id.inputUsername)).getText().toString();

        // TODO check if actually logged in (wrong password etc.)
        CheckBox rememberUser = findViewById(R.id.rememberUser);
        if (rememberUser.isChecked()) {
            SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("username", username);
            editor.putString("refresh_token", Client.getInstance().refresh_token);
            editor.apply();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}