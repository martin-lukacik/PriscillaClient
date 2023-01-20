package com.example.priscillaclient.api;

import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.models.TaskResult;

import org.json.JSONException;
import org.json.JSONObject;

public class EvaluateTask extends ApiTask {

    public EvaluateTask(FragmentBase fragment) {
        super(fragment);
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            HttpConnection connection = new HttpConnection("/task-evaluate2", "POST", true);

            JSONObject json = getJsonObject(strings);

            connection.sendRequest(json);

            if (connection.getErrorStream() != null) {
                logError(connection.getErrorStream());
                return null;
            }

            JSONObject response = new JSONObject(connection.getResponse());
            return new TaskResult(response.getJSONObject("result"));
        } catch (Exception e) {
            logError(e.getMessage());
        }

        return null;
    }

    private JSONObject getJsonObject(String[] strings) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("answer_list", strings[0]);
        json.put("activity_type", "chapter"); // TODO ??
        json.put("task_id", strings[1]);
        json.put("task_type_id", strings[2]);
        json.put("time_length", strings[3]);
        return json;
    }
}
