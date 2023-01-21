package com.example.priscillaclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Country {
    public int id;
    public String iso_code;
    public String country_name;

    public Country(JSONObject json) throws JSONException {
        id = json.getInt("id");
        iso_code = json.getString("iso_code_2");
        country_name = json.getString("country_name");
    }
}
