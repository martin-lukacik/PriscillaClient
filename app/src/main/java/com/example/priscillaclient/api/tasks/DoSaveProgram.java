package com.example.priscillaclient.api.tasks;

import com.example.priscillaclient.api.HttpConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class DoSaveProgram implements Callable<String> {

    private final int taskId;
    private final ArrayList<String> fileNames;
    private final ArrayList<String> fileContents;

    public DoSaveProgram(int taskId, ArrayList<String> fileNames, ArrayList<String> fileContents) {

        this.taskId = taskId;
        this.fileNames = fileNames;
        this.fileContents = fileContents;
    }

    @Override
    public String call() throws Exception {
        HttpConnection connection = new HttpConnection("/save-program3", "POST");

        JSONObject json = new JSONObject();

        JSONArray files = new JSONArray();
        for (int i = 0; i < fileNames.size(); ++i) {
            JSONObject file = new JSONObject();
            file.put("filename", fileNames.get(i));
            file.put("filecontent", fileContents.get(i));
            files.put(file);
        }

        json.put("files", files.toString().replaceAll("\\\\", "\\\\\\\\"));
        json.put("task_id", taskId);
        json.put("activity_type", "chapter"); // TODO hardcoded

        connection.sendRequest(json);

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        return connection.getResponse();
    }
}
