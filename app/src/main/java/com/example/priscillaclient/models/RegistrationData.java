package com.example.priscillaclient.models;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class RegistrationData {

    public ArrayList<Language> languages = new ArrayList<>();
    public ArrayList<Country> countries = new ArrayList<>();
    public ArrayList<Group> groups = new ArrayList<>();
    public ArrayList<Theme> themes = new ArrayList<>();

    public RegistrationData(JSONArray l, JSONArray c, JSONArray g, JSONArray t) throws JSONException {

        for (int i = 0; i < l.length(); ++i)
            languages.add(new Language(l.optJSONObject(i)));

        for (int i = 0; i < c.length(); ++i)
            countries.add(new Country(c.optJSONObject(i)));

        for (int i = 0; i < g.length(); ++i)
            groups.add(new Group(g.optJSONObject(i)));

        for (int i = 0; i < t.length(); ++i)
            themes.add(new Theme(t.optJSONObject(i)));

    }
}
