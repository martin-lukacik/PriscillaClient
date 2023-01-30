package com.example.priscillaclient.fragments.browse;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.fragments.FragmentAdapter;
import com.example.priscillaclient.viewmodels.browse.AreasViewModel;
import com.example.priscillaclient.viewmodels.browse.models.Area;
import com.example.priscillaclient.fragments.FragmentBase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AreasFragment extends FragmentBase implements FragmentAdapter<ArrayList<Area>> {

    // Arguments
    public static final String ARG_CATEGORY_ID = "categoryId";

    // Members
    private int categoryId = -1;
    private ArrayList<Area> areas;

    // Views
    private ListView areaListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_areas;

        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_ID);
        }

        AreasViewModel viewModel = getViewModel(AreasViewModel.class);
        viewModel.getData().observe(this, onResponse(viewModel.getError()));
        viewModel.fetchData(categoryId);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyView(findViewById(R.id.areaListView));

        areaListView = findViewById(R.id.areaListView);
        areaListView.setOnItemClickListener(this::onAreaSelected);
    }

    @Override
    public void onUpdate(ArrayList<Area> areas) {
        this.areas = areas;

        ArrayAdapter<Area> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, areas);
        areaListView.setAdapter(adapter);
    }

    private void onAreaSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle args = new Bundle();
        args.putInt(AreaCourseFragment.ARG_AREA_ID, areas.get(i).id);

        navigate(R.id.areaCourseFragment, args);
    }
}