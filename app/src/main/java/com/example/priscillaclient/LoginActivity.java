package com.example.priscillaclient;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType("com.example");

        loadingDialog = new LoadingDialog(LoginActivity.this, "Logging in...");
        loadingDialog.show();

        if (accounts.length == 0) {
            new RequestToken(this).execute(username, password, username, "password");
        } else {
            new RequestToken(this).execute(username, "refresh_token_from_store", username, "refresh_token");
        }
    }

    @Override
    public void onUpdate(Object response) {
        String username = ((EditText) findViewById(R.id.inputUsername)).getText().toString();

        // TODO check if actually logged in (wrong password etc.)
        CheckBox rememberUser = findViewById(R.id.rememberUser);
        if (rememberUser.isChecked()) {
            AccountManager am = AccountManager.get(this);
            am.addAccountExplicitly(new Account(username, "com.example"), Client.getInstance().refresh_token, null);
        }

        loadingDialog.dismiss();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}