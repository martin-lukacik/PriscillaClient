package com.example.priscillaclient;

import android.net.ConnectivityManager;
import android.net.Network;
import android.text.Html;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;


public class NetworkStateCallback extends ConnectivityManager.NetworkCallback {

    private final ActivityBase context;

    boolean restored = false;

    public NetworkStateCallback(ActivityBase context) {
        this.context = context;
    }

    @Override
    public void onAvailable(@NotNull Network network) {
        super.onAvailable(network);
        if (restored)
            Toast.makeText(context, Html.fromHtml("<span style=\"color:#008000\">Connected to internet!</span>"), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLost(@NotNull Network network) {
        super.onLost(network);
        restored = true;
        Toast.makeText(context, Html.fromHtml("<span style=\"color:#800000\">Internet connection lost!</span>"), Toast.LENGTH_LONG).show();
    }
}
