package com.example.priscillaclient.app.api;

import com.example.priscillaclient.util.HttpConnection;
import com.example.priscillaclient.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GetProgram implements Callable<Pair<ArrayList<String>, ArrayList<String>>> {

    private final int taskId;

    public GetProgram(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public Pair<ArrayList<String>, ArrayList<String>> call() throws Exception {
        HttpConnection connection = new HttpConnection("/load-program3/" + taskId, "POST");

        JSONObject json = new JSONObject();
        json.put("activity_type", "chapter"); // TODO hardcoded value "chapter"

        connection.sendRequest(json);

        if (connection.getErrorStream() != null) {
            String a = connection.getErrorMessage();
            throw new Exception(connection.getErrorMessage());
        }

        String r = connection.getResponse();
        String j = new JSONObject(r).getString("code").replaceAll("\\\\\\\\", "\\\\");
        JSONArray response = new JSONArray(j);

        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<String> fileContents = new ArrayList<>();
        for (int i = 0; i < response.length(); ++i) {
            JSONObject file = response.getJSONObject(i);
            fileNames.add(file.getString("filename"));
            fileContents.add(file.getString("filecontent"));
        }

        return new Pair<>(fileNames, fileContents);
    }
}
