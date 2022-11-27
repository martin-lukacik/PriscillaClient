package com.example.priscillaclient;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.example.priscillaclient.models.User;

import java.util.Random;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView usernameShort = findViewById(R.id.usernameShort);
        TextView usernameFull = findViewById(R.id.usernameFull);

        User user = Client.getInstance().user;

        usernameShort.setText(user.name.charAt(0) + "" + user.surname.charAt(0));
        usernameFull.setText(user.name + " " + user.surname);

        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        int color = Color.argb(255, r, g, b);

        GradientDrawable drawable = (GradientDrawable) usernameShort.getBackground();
        drawable.mutate();
        drawable.setStroke(5, color);
        usernameShort.setTextColor(color);


        TextView profileLevel = findViewById(R.id.profileLevel);
        TextView profileXP = findViewById(R.id.profileXP);
        TextView profileCoins = findViewById(R.id.profileCoins);
        TextView profileEmail = findViewById(R.id.profileEmail);

        profileLevel.setText(user.performance.level + "");
        profileXP.setText(user.performance.xp + "");
        profileCoins.setText(user.performance.coins + "");
        profileEmail.setText(user.email);
    }
}