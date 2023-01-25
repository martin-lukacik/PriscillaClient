package com.example.priscillaclient.views.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.priscillaclient.MainActivity;
import com.example.priscillaclient.R;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.Client;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public abstract class FragmentBase extends Fragment {

    protected int layoutId = 0;
    private View layout;

    protected final static Client client = Client.getInstance();

    public void navigate(int layoutId) {
        navigate(layoutId, null);
    }

    public void navigate(int layoutId, Bundle args) {
        if (getActivity() == null)
            return;

        ((MainActivity) getActivity()).navigate(layoutId, args);
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        if (layout == null)
            layout = inflater.inflate(layoutId, container, false);
        return layout;
    }

    public <T extends View> T findViewById(int id) {
        if (getActivity() == null)
            return null;
        return getActivity().findViewById(id);
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
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }
}
