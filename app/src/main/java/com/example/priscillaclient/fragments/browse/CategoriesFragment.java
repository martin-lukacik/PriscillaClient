package com.example.priscillaclient.fragments.browse;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.viewmodels.browse.CategoriesViewModel;
import com.example.priscillaclient.viewmodels.browse.models.Category;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CategoriesFragment extends FragmentBase {

    // Members
    private ArrayList<Category> categories;

    // Views
    private ListView categoryListView;

    // View models
    CategoriesViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_categories;

        viewModel = getViewModel(CategoriesViewModel.class);
        viewModel.fetchData();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryListView = findViewById(R.id.categoryListView);
        categoryListView.setOnItemClickListener(this::onCategorySelected);
        setEmptyView(categoryListView);

        viewModel.getData().observe(getViewLifecycleOwner(), this::onUpdate);
        viewModel.getErrorState().observe(getViewLifecycleOwner(), this::showError);
    }

    public void onUpdate(ArrayList<Category> categories) {
        if (categories != null) {
            this.categories = categories;
            ArrayAdapter<Category> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories);
            categoryListView.setAdapter(adapter);
        }
    }

    private void onCategorySelected(AdapterView<?> adapterView, View view, int i, long l) {
        int categoryId = categories.get(i).category_id;
        Bundle args = new Bundle();
        args.putInt("categoryId", categoryId);

        navigate(R.id.areasFragment, args);
    }
}