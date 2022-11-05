package com.example.priscillaclient;

import android.util.Log;

import com.example.priscillaclient.models.Course;
import com.example.priscillaclient.models.Language;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class APILayer {

    public Thread thread = null;

    private final String url = "https://app.priscilla.fitped.eu";

    private final int client_id = 2;
    private final String client_secret = "iQuGUAzqc187j7IKQ94tTVJAywHCAzYBGAMTxEtr";

    public void getUserCourses() {

        thread = new Thread(() -> {

        });

        thread.start();
    }

}
