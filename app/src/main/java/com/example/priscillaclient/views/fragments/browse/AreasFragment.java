package com.example.priscillaclient.views.fragments.browse;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.browse.GetAreas;
import com.example.priscillaclient.models.Area;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.views.fragments.FragmentBase;

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
        layoutId = R.layout.fragment_areas;

        new GetAreas(this, categoryId).execute();
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

        navigate(new AreaCourseFragment(area.id));
    }
}