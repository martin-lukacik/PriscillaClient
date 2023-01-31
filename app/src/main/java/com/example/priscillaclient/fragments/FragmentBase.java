package com.example.priscillaclient.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.priscillaclient.ActivityBase;
import com.example.priscillaclient.MainActivity;
import com.example.priscillaclient.R;
import com.example.priscillaclient.viewmodels.ViewModelBase;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public abstract class FragmentBase extends Fragment {

    protected int layoutId = 0;
    private View layout;

    public FragmentBase() { }

    public void navigate(int layoutId) {
        navigate(layoutId, null);
    }

    public void navigate(int layoutId, Bundle args) {
        ((MainActivity) requireActivity()).navigate(layoutId, args);
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        if (layout == null)
            layout = inflater.inflate(layoutId, container, false);
        return layout;
    }

    public void setEmptyView(AbsListView view) {
        TextView emptyView = findViewById(R.id.loadingView);
        emptyView.setText(R.string.loading);
        view.setEmptyView(emptyView);
    }

    public <T extends View> T findViewById(int id) {
        return requireActivity().findViewById(id);
    }

    public boolean isDarkModeEnabled() {
        return ((ActivityBase) requireActivity()).isDarkModeEnabled();
    }

    public String readFile(int id) {
        try {
            Resources res = getResources();
            InputStream is = res.openRawResource(id);
            byte[] b = new byte[is.available()];
            int bytes = is.read(b);
            if (bytes == -1)
                return "";
            return new String(b);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void showError(String error) {
            ((MainActivity) requireActivity()).showError(error);
    }

    protected <T extends ViewModelBase> T getViewModel(Class<T> c) {
        return ViewModelProviders.of(requireActivity()).get(c);
    }
}
