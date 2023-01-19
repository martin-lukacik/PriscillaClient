package com.example.priscillaclient.fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.HttpResponse;

public abstract class FragmentBase extends Fragment implements HttpResponse {

    public void swapFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragmentContainerView, fragment)
                .addToBackStack(null)
                .commit();
    }
}
