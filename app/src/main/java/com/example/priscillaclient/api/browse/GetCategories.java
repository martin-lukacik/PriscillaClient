package com.example.priscillaclient.api.browse;

import com.example.priscillaclient.api.ApiTaskLegacy;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetCategories extends ApiTaskLegacy {

    public GetCategories(HttpResponse context) {
        super(context);
    }

    @Override
    protected ArrayList<Category> doInBackground(String... strings) {

        // Return from cache
        if (!client.categories.isEmpty())
            return client.categories;

        try {
            HttpConnection connection = new HttpConnection("/get-categories2", "GET", false);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorMessage());
                return client.categories;
            }

            JSONArray json = new JSONObject(connection.getResponse()).getJSONArray("list");

            client.categories.clear();
            for (int i = 0; i < json.length(); ++i) {
                Category c = new Category(json.getJSONObject(i));
                client.categories.add(c);
            }

        } catch (Exception e) {
            logError(e.getMessage());
        }

        return client.categories;
    }
}
