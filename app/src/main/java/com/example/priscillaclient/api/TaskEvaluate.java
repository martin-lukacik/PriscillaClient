package com.example.priscillaclient.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.HttpURLConnectionFactory;
import com.example.priscillaclient.TaskActivity;
import com.example.priscillaclient.models.TaskEval;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class TaskEvaluate extends AsyncTask<String, String, TaskEval> {

    Context context;

    public TaskEvaluate(Context context) {
        super();
        this.context = context;
    }

    String error = null;

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
            //json.put("tasks", Client.getInstance().tasks);

            Log.i("JSON", json.toString());

            Writer writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(json.toString());
            writer.close();

           if (connection.getResponseCode() >= 400 && connection.getResponseCode() < 600) {
                InputStream is = connection.getErrorStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
                br.close();
                is.close();

                error = stringBuilder.toString();
                return null;
            }

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

        if (error != null) {
            Toast.makeText(((TaskActivity) context), error, Toast.LENGTH_LONG).show();
            return;
        }

        ((TaskActivity) context).taskEvalResponse(taskEval);
    }
}
