package com.example.priscillaclient.views.fragments;

import android.content.res.Resources;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.priscillaclient.MainActivity;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.Client;

import java.io.InputStream;

public abstract class FragmentBase extends Fragment implements HttpResponse {

    protected final static Client client = Client.getInstance();

    public void navigate(Fragment fragment) {
        if (getActivity() == null)
            return;

        ((MainActivity) getActivity()).navigate(fragment);
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
