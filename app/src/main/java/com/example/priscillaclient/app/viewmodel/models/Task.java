package com.example.priscillaclient.app.viewmodel.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Task {
    public final int task_id;
    public final int task_type_id;
    public final int score;
    public final int max_score;
    public final int first_time;
    public final int passed;
    public final int help_showed;
    public final int answer_showed;
    public final int task_order;
    public final int discuss_count;
    public final String globals;
    public String content;
    public final String start_time;
    public final String end_time;
    public final String answer;
    public final String comment;
    public final int clarity;
    public final int difficulty;

    public static int user_course_id;

    String help;
    public ArrayList<String> answers = null;
    public ArrayList<String> fakes = null;
    public ArrayList<String> codes = null;
    public ArrayList<String> fileNames = null;
    public ArrayList<String> files = null;

    public TaskType type;
    static TaskType[] taskTypes = TaskType.values();

    public Task(JSONObject json) throws JSONException {

        task_id = json.getInt("task_id");
        task_type_id = json.getInt("task_type_id");
        type = taskTypes[json.getInt("task_type_id")];
        score = json.getInt("score");
        max_score = json.getInt("max_score");
        first_time = json.getInt("first_time");
        passed = json.getInt("passed");
        help_showed = json.getInt("help_showed");
        answer_showed = json.getInt("answer_showed");
        task_order = json.getInt("task_order");
        discuss_count = json.getInt("discuss_count");
        globals = json.getString("globals");
        start_time = json.getString("start_time");
        end_time = json.getString("end_time");
        answer = json.getString("answer");
        comment = json.getString("comment");
        clarity = json.getInt("clarity");
        difficulty = json.getInt("difficulty");

        content = json.getString("content");

        try {
            JSONObject j = new JSONObject(content);
            String c = j.optString("content", null);
            content = (c == null ? j.getString("assignment") : c);

            if (j.has("help")) {
                help = json.optString("help");
            }

            if (j.has("answer_list")) {
                answers = new ArrayList<>();
                JSONArray jAnswers = j.getJSONArray("answer_list");
                for (int i = 0; i < jAnswers.length(); ++i) {
                    answers.add(jAnswers.getJSONObject(i).getString("answer"));
                }
            }

            if (j.has("fakes")) {
                fakes = new ArrayList<>();
                JSONArray jFakes = j.getJSONArray("fakes");
                for (int i = 0; i < jFakes.length(); ++i) {
                    fakes.add(jFakes.optString(i));
                }
            }

            if (j.has("codes")) {
                codes = new ArrayList<>();
                JSONArray jCodes = j.getJSONArray("codes");
                for (int i = 0; i < jCodes.length(); ++i) {
                    codes.add(jCodes.optString(i));
                }
            }


            if (type == TaskType.TASK_CODE || type == TaskType.TASK_CODE2) {

                JSONObject g = new JSONObject(globals);
                if (g.has("files")) {
                    fileNames = new ArrayList<>();
                    JSONArray gFiles = g.getJSONObject("files").getJSONArray("files");
                    for (int i = 0; i < gFiles.length(); ++i) {
                        fileNames.add(gFiles.optString(i));
                    }
                }

                if (j.has("files")) {
                    files = new ArrayList<>();
                    JSONArray jFiles = j.getJSONArray("files");
                    for (int i = 0; i < jFiles.length(); ++i) {
                        files.add(jFiles.getJSONObject(i).getString("rContent"));
                    }
                }

                content = j.getString("assignment");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
