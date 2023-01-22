package com.example.priscillaclient.api.user;

import com.example.priscillaclient.api.ApiTask;
import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.api.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeProfile extends ApiTask {

    public ChangeProfile(HttpResponse context) {
        super(context);
    }

    @Override
    protected Object doInBackground(String... strings) {

        int age = Integer.parseInt(strings[0]);
        int content_type_id = Integer.parseInt(strings[1]);
        int country = Integer.parseInt(strings[2]);
        String group = strings[3];
        int lang = Integer.parseInt(strings[4]);
        String name = strings[5];
        String nick = strings[6];
        String surname = strings[7];
        int theme_id = Integer.parseInt(strings[8]);

        try {
            HttpConnection connection = new HttpConnection("/profile-change", "POST", true);

            JSONObject json = new JSONObject();
            json.put("age", age);
            json.put("content_type_id", content_type_id);
            json.put("country", country);
            json.put("group", group);
            json.put("lang", lang);
            json.put("name", name);
            json.put("nick", nick);
            json.put("surname", surname);
            json.put("theme_id", theme_id);

            connection.sendRequest(json);

            // Invalidate cache
            client.profile = null;

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorStream());
                return null;
            }

        } catch (Exception e) {
            logError(e.getMessage());
        }
        return null;
    }
}
