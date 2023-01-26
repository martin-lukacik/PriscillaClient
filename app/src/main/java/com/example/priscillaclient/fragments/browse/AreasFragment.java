package com.example.priscillaclient.fragments.browse;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.browse.GetAreas;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.models.Area;
import com.example.priscillaclient.models.Client;

import java.util.ArrayList;

public class AreasFragment extends FragmentBase implements HttpResponse<Object> {

    int categoryId = -1;

    public AreasFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_areas;

        if (getArguments() != null) {
            categoryId = getArguments().getInt("categoryId");
        }

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
        Bundle args = new Bundle();
        args.putInt("areaId", client.areas.get(i).id);

        navigate(R.id.areaCourseFragment, args);
    }
}