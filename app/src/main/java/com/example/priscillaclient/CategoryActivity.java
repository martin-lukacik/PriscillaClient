package com.example.priscillaclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.api.browse.GetCategories;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.models.Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CategoryActivity extends ActivityBase implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(this::onMenuItemSelected);
        navigationView.getMenu().findItem(R.id.menu_all_courses).setChecked(true);

        new GetCategories(this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().findItem(R.id.menu_all_courses).setChecked(true);
    }

    @Override
    public void onUpdate(Object response) {
        ArrayList<Category> categories = Client.getInstance().categories;

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        ListView categoryListView = findViewById(R.id.categoryListView);
        categoryListView.setAdapter(adapter);
        categoryListView.setOnItemClickListener(this::categorySelected);
    }

    private void categorySelected(AdapterView<?> adapterView, View view, int i, long l) {
        // https://app.priscilla.fitped.eu/get-areas/{CATEGORY_ID}

        Intent intent = new Intent(CategoryActivity.this, AreaActivity.class);
        intent.putExtra("category_id", Client.getInstance().categories.get(i).category_id);
        startActivity(intent);
    }
}