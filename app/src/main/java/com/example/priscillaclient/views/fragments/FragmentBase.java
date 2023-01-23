package com.example.priscillaclient.views.fragments;

import android.content.res.Resources;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.priscillaclient.ActivityBase;
import com.example.priscillaclient.R;
import com.example.priscillaclient.api.HttpResponse;

import java.io.InputStream;

public abstract class FragmentBase extends Fragment implements HttpResponse {

    public void swapFragment(Fragment fragment) {
        if (getActivity() == null)
            return;

        ((ActivityBase) getActivity()).swapFragment(fragment);
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
}
