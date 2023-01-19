package com.example.priscillaclient.api;

import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.models.TaskEval;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class TaskEvaluate extends ApiTask {

    public TaskEvaluate(FragmentBase fragment) {
        super(fragment);
    }

    @Override
    protected TaskEval doInBackground(String... strings) {
        try {
            HttpURLConnection connection = getConnection("/task-evaluate2", "POST", true);

            JSONObject json = new JSONObject();
            json.put("answer_list", strings[0]);
            json.put("activity_type", "chapter"); // TODO ??
            json.put("task_id", strings[1]);
            json.put("task_type_id", strings[2]);
            json.put("time_length", strings[3]);

            Writer writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(json.toString());
            writer.close();

            int status = connection.getResponseCode();
            if (status >= 400 && status < 600) {
                logError(connection.getErrorStream());
                return null;
            }

            // RESPONSE

            InputStream responseStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            responseStreamReader.close();

            JSONObject response = new JSONObject(stringBuilder.toString());
            return new TaskEval(response.getJSONObject("result"));
        } catch (Exception e) {
            logError(e.getMessage());
        }

        return null;
    }
}
