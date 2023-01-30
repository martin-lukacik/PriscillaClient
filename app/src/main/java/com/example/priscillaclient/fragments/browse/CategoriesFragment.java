package com.example.priscillaclient.fragments.browse;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.fragments.FragmentAdapter;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.viewmodels.browse.CategoriesViewModel;
import com.example.priscillaclient.viewmodels.browse.models.Category;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CategoriesFragment extends FragmentBase implements FragmentAdapter<ArrayList<Category>> {

    private ArrayList<Category> categories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_categories;

        CategoriesViewModel viewModel = (CategoriesViewModel) getViewModel(CategoriesViewModel.class);
        viewModel.getData().observe(this, onResponse(viewModel.getError()));
        viewModel.fetchData();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEmptyView(findViewById(R.id.categoryListView));
    }

    public void onUpdate(ArrayList<Category> categories) {

        this.categories = categories;

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories);
        ListView categoryListView = findViewById(R.id.categoryListView);
        categoryListView.setAdapter(adapter);
        categoryListView.setOnItemClickListener(this::categorySelected);
    }

    private void categorySelected(AdapterView<?> adapterView, View view, int i, long l) {
        int categoryId = categories.get(i).category_id;
        Bundle args = new Bundle();
        args.putInt("categoryId", categoryId);
        navigate(R.id.areasFragment, args);
    }
}