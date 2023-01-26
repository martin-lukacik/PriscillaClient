package com.example.priscillaclient.user.viewmodel.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Country {
    public final int id;
    public final String iso_code;
    public final String country_name;

    public Country(JSONObject json) throws JSONException {
        id = json.getInt("id");
        iso_code = json.getString("iso_code_2");
        country_name = json.getString("country_name");
    }
}
