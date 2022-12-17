package com.example.priscillaclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.api.GetCategories;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.models.Category;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        new GetCategories(this).execute();
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