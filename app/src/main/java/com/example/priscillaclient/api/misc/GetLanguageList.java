package com.example.priscillaclient.api.misc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.view.menu.MenuBuilder;

import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.models.Language;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

// TODO do we even need this? probs for quiz translations
public class GetLanguageList extends AsyncTask<String, String, ArrayList<Language>> {

    Context context;
    Menu menu;

    public GetLanguageList(Context context, Menu menu) {
        super();
        this.context = context;
        this.menu = menu;
    }

    @Override
    public ArrayList<Language> doInBackground(String... strings) {

        try {
            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/languages", "GET", false);

            int status = connection.getResponseCode();
            String message = connection.getResponseMessage();

            InputStream responseStream = connection.getInputStream();

            String responseStr = "";
            try (Scanner scanner = new Scanner(responseStream)) {
                responseStr = scanner.useDelimiter("\\A").next();
            }

            Log.i("LANGUAGES", responseStr);

            Client client = Client.getInstance();
            client.languageList = new ArrayList<>();

            JSONArray json = new JSONArray(responseStr);

            for (int i = 0; i < json.length(); ++i) {
                client.languageList.add(new Language((JSONObject) json.get(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Client.getInstance().languageList;
    }

    @SuppressLint("RestrictedApi") // TODO restricted api
    protected void onPostExecute(ArrayList<Language> languages) {

        if (menu instanceof MenuBuilder) {

            ((MenuBuilder) menu).setOptionalIconsVisible(true);

            for (int i = 0; i < languages.size(); ++i) {
                int id = ((MenuBuilder) menu).getContext().getResources().getIdentifier("flag_" + languages.get(i).shortcut.toLowerCase(), "drawable", ((MenuBuilder) menu).getContext().getPackageName());
                menu.add(languages.get(i).name).setIcon(id);
            }
        }
    }
}
