package com.example.priscillaclient.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.browse.GetAreas;
import com.example.priscillaclient.models.Area;
import com.example.priscillaclient.models.Category;
import com.example.priscillaclient.models.Client;

import java.util.ArrayList;

public class AreasFragment extends FragmentBase {

    int categoryId = -1;

    public AreasFragment() { }

    public AreasFragment(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new GetAreas(this, categoryId).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_areas, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<Area> areas = Client.getInstance().areas;
        if (!areas.isEmpty())
            onUpdate(areas);
    }

    @Override
    public void onUpdate(Object response) {
        ArrayList<Area> areas = Client.getInstance().areas;
        ArrayAdapter<Area> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, areas);
        ListView areaListView = findViewById(R.id.areaListView);
        areaListView.setAdapter(adapter);
        areaListView.setOnItemClickListener(this::areaSelected);
    }

    private void areaSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Area area = Client.getInstance().areas.get(i);

        swapFragment(new AreaCourseFragment(area.id));
    }
}