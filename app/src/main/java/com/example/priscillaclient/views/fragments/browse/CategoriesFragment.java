package com.example.priscillaclient.views.fragments.browse;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.browse.GetCategories;
import com.example.priscillaclient.models.Category;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.views.fragments.FragmentBase;

import java.util.ArrayList;

public class CategoriesFragment extends FragmentBase implements HttpResponse<Object> {

    public CategoriesFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_categories;

        new GetCategories(this).execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<Category> categories = Client.getInstance().categories;
        if (!categories.isEmpty())
            onUpdate(categories);
    }

    @Override
    public void onUpdate(Object response) {

        ArrayList<Category> categories = Client.getInstance().categories;
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories);
        ListView categoryListView = findViewById(R.id.categoryListView);
        categoryListView.setAdapter(adapter);
        categoryListView.setOnItemClickListener(this::categorySelected);
    }

    private void categorySelected(AdapterView<?> adapterView, View view, int i, long l) {
        int categoryId = Client.getInstance().categories.get(i).category_id;
        Bundle args = new Bundle();
        args.putInt("categoryId", categoryId);
        navigate(R.id.areasFragment, args);
    }
}