package com.example.priscillaclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.priscillaclient.api.RequestToken;
import com.example.priscillaclient.views.LoadingDialog;

public class LoginActivity extends AppCompatActivity {

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

        loadingDialog = new LoadingDialog(LoginActivity.this, "Logging in...");
        loadingDialog.show();

        new RequestToken(this, loadingDialog)
                .execute(username, password, username);
    }
}