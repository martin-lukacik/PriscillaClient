package com.example.priscillaclient.api.user;

import com.example.priscillaclient.api.HttpConnection;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class DoUpdate implements Callable<String> {

    final int age;
    final int content_type_id;
    final int country;
    final String group;
    final int lang;
    final String name;
    final String nick;
    final String surname;
    final int theme_id;

    public DoUpdate(int age, int content_type_id, int country, String group, int lang, String name, String nick, String surname, int theme_id) {
        this.age = age;
        this.content_type_id = content_type_id;
        this.country = country;
        this.group = group;
        this.lang = lang;
        this.name = name;
        this.nick = nick;
        this.surname = surname;
        this.theme_id = theme_id;
    }

    @Override
    public String call() throws Exception {
        HttpConnection connection = new HttpConnection("/profile-change", "POST");

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

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        throw new Exception(connection.getResponse());
    }
}
