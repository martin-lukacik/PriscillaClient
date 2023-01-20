package com.example.priscillaclient.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.priscillaclient.api.client.Client;
import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.MainActivity;
import com.example.priscillaclient.models.User;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Scanner;

public class GetUserParams extends AsyncTask<String, String, User> {

    Context context;

    public GetUserParams(Context context) {
        super();
        this.context = context;
    }

    protected User doInBackground(String... strings) {
        return getUserParams();
    }

    protected void onPostExecute(User user) {
        ((MainActivity) this.context).onUpdate(user);
    }

    protected User getUserParams() {
        try {
            Log.i("DEBUG", "PASS");
            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/get-full-user-parameters", "GET", false);

            int status = connection.getResponseCode();
            String message = connection.getResponseMessage();

            Log.i("STATUS", status + " MESSAGE: " + message);
            InputStream responseStream = connection.getInputStream();

            String responseStr;
            try (Scanner scanner = new Scanner(responseStream)) {
                responseStr = scanner.useDelimiter("\\A").next();
            }

            JSONObject response = new JSONObject(responseStr);
            Log.i("RESPONSE", response.toString());

            Client client = Client.getInstance();
            client.user = new User(response);

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Client.getInstance().user; // TODO need return?
    }
}
