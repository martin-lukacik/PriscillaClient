package com.example.priscillaclient.fragments.browse;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.viewmodels.browse.models.Category;
import com.example.priscillaclient.viewmodels.browse.CategoriesViewModel;
import com.example.priscillaclient.util.LoadingDialog;

import java.util.ArrayList;

public class CategoriesFragment extends FragmentBase {

    ArrayList<Category> categories;

    public CategoriesFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_categories;

        dialog = new LoadingDialog(getActivity());

        CategoriesViewModel viewModel = (CategoriesViewModel) getViewModel(CategoriesViewModel.class);
        viewModel.getData().observe(this, (data) -> {
            if (viewModel.hasError())
                showError(viewModel.getError());
            else if (data != null) {
                onUpdate(data);
                dialog.dismiss();
            }
        });
        viewModel.fetchData();

        if (viewModel.getData().getValue() == null || viewModel.getData().getValue().isEmpty())
            dialog.show();
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