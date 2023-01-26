package com.example.priscillaclient.browse.api;

import com.example.priscillaclient.util.HttpConnection;
import com.example.priscillaclient.browse.viewmodel.models.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GetCategories implements Callable<ArrayList<Category>> {
    @Override
    public ArrayList<Category> call() throws Exception {
        HttpConnection connection = new HttpConnection("/get-categories2", "GET");

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONArray json = new JSONObject(connection.getResponse()).getJSONArray("list");

        ArrayList<Category> categories = new ArrayList<>();
        for (int i = 0; i < json.length(); ++i) {
            Category c = new Category(json.getJSONObject(i));
            categories.add(c);
        }

        return categories;
    }
}
