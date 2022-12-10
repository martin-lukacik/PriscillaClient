package com.example.priscillaclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.api.GetAreas;
import com.example.priscillaclient.api.GetCategories;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.models.Area;
import com.example.priscillaclient.models.Category;

import java.util.ArrayList;

public class AreaActivity extends AppCompatActivity implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        Intent intent = getIntent();
        int categoryId = intent.getIntExtra("category_id", -1);

        new GetAreas(this, categoryId).execute();
    }

    @Override
    public void onUpdate(Object response) {
        ArrayList<Area> areas = (ArrayList<Area>) response;

        ArrayAdapter<Area> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, areas);
        ListView areaListView = findViewById(R.id.areaListView);
        areaListView.setAdapter(adapter);
        areaListView.setOnItemClickListener(this::areaSelected);
    }

    private void areaSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Area area = Client.getInstance().areas.get(i);

        Intent intent = new Intent(AreaActivity.this, AreaCourseActivity.class);
        intent.putExtra("area_id", area.id);
        startActivity(intent);
    }

}