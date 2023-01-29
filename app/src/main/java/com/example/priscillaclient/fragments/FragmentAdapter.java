package com.example.priscillaclient.fragments;

import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.example.priscillaclient.R;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import java.util.ArrayList;

public interface FragmentAdapter<T> {

    void onUpdate(T response);

    default Observer<T> onResponse(ViewModelBase viewModel) {
        FragmentBase context = (FragmentBase) this;
        return response -> {
            if (viewModel.hasError())
                context.showError(viewModel.getError());
            else if (response != null) {
                if (response instanceof ArrayList && ((ArrayList<?>) response).isEmpty()) {
                    ((TextView) context.findViewById(R.id.loadingView)).setText(R.string.no_results);
                } else {
                    onUpdate(response);
                }
            }
        };
    }
}