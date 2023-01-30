package com.example.priscillaclient.fragments;

import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.example.priscillaclient.R;

import java.util.ArrayList;

public interface FragmentAdapter<T> {

    void onUpdate(T response);

    default Observer<T> onResponse(String error) {
        FragmentBase context = (FragmentBase) this;
        return response -> {
            if (error != null) {
                context.showError(error);
                ((TextView) context.findViewById(R.id.loadingView)).setText(error);
            } else if (response != null) {
                if (response instanceof ArrayList && ((ArrayList<?>) response).isEmpty()) {
                    ((TextView) context.findViewById(R.id.loadingView)).setText(R.string.no_results);
                } else {
                    onUpdate(response);
                }
            }
        };
    }
}