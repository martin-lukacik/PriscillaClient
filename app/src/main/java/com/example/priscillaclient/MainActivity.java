package com.example.priscillaclient;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.priscillaclient.api.GetUserCoursesTask;
import com.example.priscillaclient.api.GetUserParamsTask;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.Course;
import com.example.priscillaclient.models.User;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements HttpResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }

        new GetUserParamsTask(this).execute();
        new GetUserCoursesTask(this).execute();
    }

    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://app.priscilla.fitped.eu/oauth/token");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("client_id", 2);
                    jsonParam.put("client_secret", "iQuGUAzqc187j7IKQ94tTVJAywHCAzYBGAMTxEtr");
                    jsonParam.put("email", "martin.lukacik@student.ukf.sk");
                    jsonParam.put("grant_type", "password");
                    jsonParam.put("password", "9701092533");
                    jsonParam.put("username", "martin.lukacik@student.ukf.sk");

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    // RESPONSE

                    InputStream responseStream = new BufferedInputStream(conn.getInputStream());
                    BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = responseStreamReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    responseStreamReader.close();

                    String response = stringBuilder.toString();
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.i("RESPONSe", response.toString());


                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void getData(View view) {
        new GetUserParamsTask(this)
                .execute();
        /*APILayer api = new APILayer();
        api.getUserParameters();
        while (api.thread.isAlive());
        TextView tv = findViewById(R.id.output);
        tv.setText(Client.getInstance().user.name + " " + Client.getInstance().user.surname);
    */}

    CourseListAdapter adapter;

    @Override
    public void onUpdate(Object response) {

        super.onUpdate(response);

        if (response instanceof ArrayList<?>) {
            ArrayList<Course> courses = (ArrayList<Course>) response;
            GridView courseListView = findViewById(R.id.courseListView);

            adapter = new CourseListAdapter(this, courses);
            courseListView.setAdapter(adapter);
            courseListView.setOnItemClickListener(this::courseSelected);
        }
    }

    private void courseSelected(AdapterView<?> adapterView, View view, int i, long l){
        Bundle bundle = new Bundle();
        int courseId = Client.getInstance().courses.get(i).course_id;
        bundle.putInt("course_id", courseId);

        Intent intent = new Intent(MainActivity.this, ChapterActivity.class);
        intent.putExtra("course_id", courseId);
        startActivity(intent);
    }
}