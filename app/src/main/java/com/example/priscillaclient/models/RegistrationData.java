package com.example.priscillaclient.models;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class RegistrationData {

    public final ArrayList<Language> languages = new ArrayList<>();
    public final ArrayList<Country> countries = new ArrayList<>();
    public final ArrayList<Group> groups = new ArrayList<>();
    public final ArrayList<Theme> themes = new ArrayList<>();

    public RegistrationData() { }

    public void set(JSONArray l, JSONArray c, JSONArray g, JSONArray t) throws JSONException {

        clear();

        for (int i = 0; i < l.length(); ++i)
            languages.add(new Language(l.optJSONObject(i)));

        for (int i = 0; i < c.length(); ++i)
            countries.add(new Country(c.optJSONObject(i)));

        for (int i = 0; i < g.length(); ++i)
            groups.add(new Group(g.optJSONObject(i)));

        for (int i = 0; i < t.length(); ++i)
            themes.add(new Theme(t.optJSONObject(i)));

    }

    public void clear() {
        languages.clear();
        countries.clear();
        groups.clear();
        themes.clear();
    }

    public boolean isEmpty() {
        return (languages.isEmpty() && countries.isEmpty() && groups.isEmpty() && themes.isEmpty());
    }

    public Pair<Integer, String[]> getCountrySelection() {
        Profile profile = Client.getInstance().profile;
        int selectedIndex = 0;
        String[] items = new String[countries.size()];
        for (int i = 0; i < countries.size(); ++i) {
            items[i] = countries.get(i).country_name;

            if (countries.get(i).id == profile.country_id) {
                selectedIndex = i;
            }
        }
        return new Pair<>(selectedIndex, items);
    }

    public Pair<Integer, String[]> getGroupSelection() {
        Profile profile = Client.getInstance().profile;
        int selectedIndex = 0;
        String[] items = new String[groups.size()];
        for (int i = 0; i < groups.size(); ++i) {
            items[i] = groups.get(i).group_name;

            if (groups.get(i).group_name.equals(profile.groups)) {
                selectedIndex = i;
            }
        }
        return new Pair<>(selectedIndex, items);
    }

    public Pair<Integer, String[]> getLanguageSelection() {
        Profile profile = Client.getInstance().profile;
        int selectedIndex = 0;
        String[] items = new String[languages.size()];
        for (int i = 0; i < languages.size(); ++i) {
            items[i] = languages.get(i).name;

            if (languages.get(i).id == profile.pref_lang_id) {
                selectedIndex = i;
            }
        }
        return new Pair<>(selectedIndex, items);
    }

    public Pair<Integer, String[]> getThemeSelection() {
        Profile profile = Client.getInstance().profile;
        int selectedIndex = 0;
        String[] items = new String[themes.size()];
        for (int i = 0; i < themes.size(); ++i) {
            items[i] = themes.get(i).theme_name;

            if (themes.get(i).id == profile.theme_id) {
                selectedIndex = i;
            }
        }
        return new Pair<>(selectedIndex, items);
    }
}
