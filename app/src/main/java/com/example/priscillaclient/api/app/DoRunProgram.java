package com.example.priscillaclient.api.app;

import com.example.priscillaclient.api.HttpConnection;
import com.example.priscillaclient.api.WssListener;
import com.example.priscillaclient.viewmodels.app.models.Task;
import com.example.priscillaclient.viewmodels.app.models.TaskResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DoRunProgram implements Callable<TaskResult> {

    private final int exeType;
    private final Task task;
    private final ArrayList<String> fileNames;
    private final ArrayList<String> fileContents;
    private final int timeLength;

    private final CountDownLatch latch = new CountDownLatch(1);

    public DoRunProgram(int exeType, Task task, ArrayList<String> fileNames, ArrayList<String> fileContents, int timeLength) {
        this.exeType = exeType;
        this.task = task;
        this.fileNames = fileNames;
        this.fileContents = fileContents;
        this.timeLength = timeLength;
    }

    @Override
    public TaskResult call() throws Exception {
        HttpConnection connection = new HttpConnection("/vpl-run-test3", "POST");

        JSONObject json = new JSONObject();

        JSONArray files = new JSONArray();
        for (int i = 0; i < fileNames.size(); ++i) {
            JSONObject file = new JSONObject();
            file.put("filename", fileNames.get(i));
            file.put("filecontent", fileContents.get(i));
            files.put(file);
        }

        json.put("submitted_files", files.toString());
        json.put("exe_type", exeType);
        json.put("task_id", task.task_id);

        connection.sendRequest(json);

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONObject response = new JSONObject(connection.getResponse());

        String adminTicket = response.getString("adminticket");
        String monitorticket = response.getString("monitorticket");
        /* String executionticket = response.getString("executionticket"); */

        return getResult(adminTicket, monitorticket);
    }

    private TaskResult getResult(String adminTicket, String monitorTicket) throws Exception {
        // Monitor execution progress
        WssListener listener = new WssListener(latch);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("wss://vpl.ki.fpvai.ukf.sk/" + monitorTicket + "/monitor")
                .build();
        client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();

        // Await execution result
        try {
            latch.await();
        } catch (Exception ignore) { }

        // Get evaluation
        HttpConnection connection = new HttpConnection("/vpl-get-result33", "POST");

        JSONObject json = new JSONObject();

        json.put("activity_type", "chapter");
        json.put("adminticket", adminTicket);
        json.put("task_id", task.task_id);
        json.put("task_type_id", task.task_type_id);
        json.put("tests_description", true);
        json.put("time_length", timeLength);

        connection.sendRequest(json);

        if (connection.getErrorStream() != null) {
            throw new Exception(connection.getErrorMessage());
        }

        JSONObject response = new JSONObject(connection.getResponse());

        return new TaskResult(response);
    }
}
