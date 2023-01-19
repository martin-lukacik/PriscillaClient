package com.example.priscillaclient.fragments;

import android.content.res.Resources;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.HttpResponse;

import java.io.InputStream;

public abstract class FragmentBase extends Fragment implements HttpResponse {

    public void swapFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragmentContainerView, fragment)
                .addToBackStack(null)
                .commit();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById(int id) {
        if (getActivity() == null)
            return null;
        return (T) getActivity().findViewById(id);
    }

    public String readFile(int id) {
        try {
            Resources res = getResources();
            InputStream is = res.openRawResource(id);
            byte[] b = new byte[is.available()];
            is.read(b);
            return new String(b);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
