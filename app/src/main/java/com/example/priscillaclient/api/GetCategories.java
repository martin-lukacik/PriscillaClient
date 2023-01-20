package com.example.priscillaclient.api;

import android.content.Context;
import android.os.AsyncTask;

import com.example.priscillaclient.CategoryActivity;
import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.api.client.Client;
import com.example.priscillaclient.models.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class GetCategories extends AsyncTask<String, String, ArrayList<Category>> {

    final Context context;

    public GetCategories(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected ArrayList<Category> doInBackground(String... strings) {

        try {
            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/get-categories2", "GET", false);

            int status = connection.getResponseCode();
            String message = connection.getResponseMessage();

            InputStream responseStream = connection.getInputStream();

            String responseStr = "";
            try (Scanner scanner = new Scanner(responseStream)) {
                responseStr = scanner.useDelimiter("\\A").next();
            }


            JSONArray json = new JSONObject(responseStr).getJSONArray("list");

            Client client = Client.getInstance();

            client.categories = new ArrayList<>();

            for (int i = 0; i < json.length(); ++i) {
                Category c = new Category(json.getJSONObject(i));
                client.categories.add(c);
            }

            return client.categories;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Category> categories) {
        ((CategoryActivity) context).onUpdate(categories);
    }
}
