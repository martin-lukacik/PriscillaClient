package com.example.priscillaclient.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.TaskActivity;
import com.example.priscillaclient.models.TaskEval;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class TaskEvaluate extends AsyncTask<String, String, TaskEval> {

    Context context;

    public TaskEvaluate(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected TaskEval doInBackground(String... strings) {
        Log.i("TASK_EVAL", "Evaluating task");
        try {
            HttpURLConnection connection = HttpURLConnectionFactory.getConnection("/task-evaluate2", "POST", true);

            JSONObject json = new JSONObject();
            json.put("answer_list", strings[0]);
            json.put("activity_type", "chapter"); // TODO ??
            json.put("task_id", strings[1]);
            json.put("task_type_id", strings[2]);
            json.put("time_length", strings[3]);

            Log.i("TASK_EVAL", "Building json");

            Log.i("JSON", json.toString());
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(json.toString());

            os.flush();
            os.close();

            Log.i("TASK_STATUS", String.valueOf(connection.getResponseCode()));
            Log.i("TASK_MSG", connection.getResponseMessage());

            // RESPONSE

            InputStream responseStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            responseStreamReader.close();

            JSONObject response = new JSONObject(stringBuilder.toString());
            Log.i("TASK_RESPONSE", response.toString());

            connection.disconnect();

            return new TaskEval(response.getJSONObject("result"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(TaskEval taskEval) {
        super.onPostExecute(taskEval);
        ((TaskActivity) context).onUpdate(taskEval);
    }
}
