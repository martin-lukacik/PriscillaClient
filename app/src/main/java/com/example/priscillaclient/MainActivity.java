package com.example.priscillaclient;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.priscillaclient.api.GetUserCourses;
import com.example.priscillaclient.api.GetUserParams;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.fragments.ChapterFragment;
import com.example.priscillaclient.fragments.CoursesFragment;
import com.example.priscillaclient.models.Course;
import com.example.priscillaclient.models.User;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }

        swapFragment(new CoursesFragment());

        new GetUserParams(this).execute();
    }


    private void swapFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragmentContainerView, fragment)
                //.addToBackStack(null)
                .commit();
    }

    CourseListAdapter adapter;

    @Override
    public void onUpdate(Object response) {

        super.onUpdate(response);

        if (response instanceof User) {
            super.setMenuTitle((User) response);
        }
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            // TODO toto je hack - ak sa vrati user do aplikacie, hodi ho na login (prvu aktivitu)
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);

            // TOTO nie je hack
            //super.onBackPressed();
        }
    }
}