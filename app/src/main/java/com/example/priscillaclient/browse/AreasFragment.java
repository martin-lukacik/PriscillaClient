package com.example.priscillaclient.browse;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.browse.viewmodel.AreasViewModel;
import com.example.priscillaclient.browse.viewmodel.models.Area;
import com.example.priscillaclient.util.FragmentBase;
import com.example.priscillaclient.util.LoadingDialog;

import java.util.ArrayList;

public class AreasFragment extends FragmentBase {

    ArrayList<Area> areas;
    int categoryId = -1;

    public AreasFragment() { }

    boolean firstLoad = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_areas;

        if (getArguments() != null) {
            categoryId = getArguments().getInt("categoryId");
        }

        dialog = new LoadingDialog(getActivity());

        AreasViewModel viewModel = (AreasViewModel) getViewModel(AreasViewModel.class);
        viewModel.getData().observe(this, (data) -> {
            if (viewModel.hasError())
                showError(viewModel.getError());
            else if (data != null && !firstLoad) {
                dialog.dismiss();
                onUpdate(data);
            }
            firstLoad = false;
        });
        viewModel.fetchData(categoryId);
        dialog.show();
    }

    public void onUpdate(ArrayList<Area> areas) {

        this.areas = areas;
        ArrayAdapter<Area> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, areas);
        ListView areaListView = findViewById(R.id.areaListView);
        areaListView.setAdapter(adapter);
        areaListView.setOnItemClickListener(this::areaSelected);
    }

    private void areaSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle args = new Bundle();
        args.putInt("areaId", areas.get(i).id);

        navigate(R.id.areaCourseFragment, args);
    }
}