package com.example.priscillaclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.priscillaclient.api.GetActiveLessons;
import com.example.priscillaclient.api.GetActiveTasks;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.TaskEvaluate;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.models.Lesson;
import com.example.priscillaclient.models.Task;
import com.example.priscillaclient.models.TaskEval;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity implements HttpResponse {

    int courseId;
    int chapterId;

    int currentLesson = -1;
    int currentLessonId = -1;

    int currentTask = 0;

    boolean updateLayout = true;

    LinearLayout taskLayout;
    WebView webView;
    EditText inputEditText;

    TaskViewInterface jsInterface = new TaskViewInterface(this);

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        taskLayout = findViewById(R.id.taskLayout);
        inputEditText = findViewById(R.id.inputEditText);
        inputEditText.setVisibility(View.GONE);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(jsInterface, "Android");

        Intent intent = getIntent();

        courseId = intent.getIntExtra("course_id", -1);
        chapterId = intent.getIntExtra("chapter_id", -1);

        currentLesson = intent.getIntExtra("current_lesson", -1);
        currentLessonId = intent.getIntExtra("current_lesson_id", -1);
        currentTask = intent.getIntExtra("current_task", 0);

        new GetActiveLessons(this, chapterId).execute();

        if (currentLesson != -1 && currentLessonId != -1) {
            new GetActiveTasks(this, courseId, chapterId, currentLessonId).execute();
        }
    }

    public void updateLessons(ArrayList<Lesson> lessons) {

        if (currentLesson == -1 || currentLessonId == -1) {
            new GetActiveTasks(this, courseId, chapterId, lessons.get(0).id).execute();
        } else {
            new GetActiveTasks(this, courseId, chapterId, currentLessonId).execute();
        }

        if (updateLayout) {
            updateLayout = false;
            NavigationView navigationView = findViewById(R.id.navigationView);
            Menu menu = navigationView.getMenu();
            menu.clear();

            menu.add("Lessons");
            menu.getItem(0).setEnabled(false);

            navigationView.bringToFront();

            for (Lesson lesson : lessons) {
                MenuItem item = menu.add(lesson.name);

                item.setOnMenuItemClickListener((e) -> {
                    currentLesson = item.getItemId();
                    currentLessonId = lesson.id;
                    onUpdate(lessons);
                    DrawerLayout drawer = findViewById(R.id.drawerLayout);
                    drawer.closeDrawers();
                    return false;
                });
            }

            navigationView.invalidate();
        }
    }

    public void updateTasks(ArrayList<Task> tasks) {
        clearTaskLayout();

        Task task = tasks.get(currentTask);

        String css = "<style>.answer { max-width:4em } pre{ background:lightgrey; color:black; overflow-y:scroll; }</style>";
        String javascript = "<script>function process() { let arr = []; let els = document.querySelectorAll(\".answer\"); for (let i = 0; i < els.length; ++i) { arr.push(els[i].value); } Android.sendData(JSON.stringify(arr)); }</script>";

        String content = task.content;

        switch (task.type) {
            case TASK_CHOICE:
                if (task.answers != null) {
                    RadioGroup radioGroup = new RadioGroup(this);
                    radioGroup.setTag("CLEAR");

                    for (String answer : task.answers) {
                        RadioButton radioButton = new RadioButton(this);
                        radioButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        radioButton.setText(answer);
                        radioGroup.addView(radioButton);
                    }
                    taskLayout.addView(radioGroup);
                }
                break;
            case TASK_FILL:
                content = task.content.replaceAll("§§_§§", "<input class=\"answer\" type=\"text\" oninput=\"process();\">");
                break;

            case TASK_INPUT:
                inputEditText.setVisibility(View.VISIBLE);
                break;
            case TASK_MULTI:
                if (task.answers != null) {
                    for (String answer : task.answers) {
                        CheckBox checkBox = new CheckBox(this);
                        checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        checkBox.setText(answer);
                        checkBox.setTag("CLEAR");
                        taskLayout.addView(checkBox);
                    }
                }
                break;
            case TASK_DRAG:

                content = task.content.replaceAll("§§_§§", "<span onclick=\"return remove(this);\" class=\"drag\" style=\"text-align:center; color:white; display:inline-block; background:black; width:4em\"></span>");

                javascript += "<script>function add(el) {\n" +
                        "\n" +
                        "\tif (el.disabled) {\n" +
                        "\t\treturn;\n" +
                        "\t}\n" +
                        "\n" +
                        "\tlet els = document.getElementsByTagName(\"span\");\n" +
                        "\n" +
                        "\tfor (let i = 0; i < els.length; ++i) {\n" +
                        "\t\tif (els[i].innerText == \"\") {\n" +
                        "\t\t\tels[i].innerText = el.innerText;\n" +
                        "\t\t\tel.disabled = true;\n" +
                        "\t\t\tbreak;\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "\n" +
                        "\tlet arr = [];\n" +
                        "\tfor (let i = 0; i < els.length; ++i) {\n" +
                        "\t\tarr.push(els[i].innerText);\n" +
                        "\t}\n" +
                        "\n" +
                        "\tAndroid.sendData(JSON.stringify(arr));\n" +
                        "}\n" +
                        "\n" +
                        "function remove(el) {\n" +
                        "\t\n" +
                        "\tif (el.innerText == \"\") {\n" +
                        "\t\treturn;\n" +
                        "\t}\n" +
                        "\n" +
                        "\tlet els = document.getElementsByTagName(\"button\");\n" +
                        "\n" +
                        "\tfor (let i = 0; i < els.length; ++i) {\n" +
                        "\t\tif (els[i].disabled && els[i].innerText == el.innerText) {\n" +
                        "\t\t\tels[i].disabled = false;\n" +
                        "\t\t\tbreak;\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "\n" +
                        "\tel.innerText = \"\";\n" +
                        "}</script>";

                String html = "";
                if (task.fakes != null) {
                    html = "<hr>";
                    for (String fake : task.fakes) {
                        html += "<button onclick=\"return add(this);\">" + fake + "</button>";
                    }
                }

                content += html;

                break;
        }

        webView.loadData(css + javascript + content, "text/html; charset=utf-8", "UTF-8");
    }

    @Override
    public void onUpdate(Object response) {
        if (((ArrayList<?>) response).get(0) instanceof Lesson)
            updateLessons((ArrayList<Lesson>) response);
        else if (((ArrayList<?>) response).get(0) instanceof Task) {
            currentTask = 0;
            updateTasks((ArrayList<Task>) response);
        }
    }

    public void taskEvalResponse(TaskEval taskEval) {
        Toast.makeText(this, "Rating: " + taskEval.rating, Toast.LENGTH_SHORT).show();
    }

    public void clearTaskLayout() {
        inputEditText.setText("");
        inputEditText.setVisibility(View.GONE);
        for (int i = taskLayout.getChildCount() - 1; i >= 0; --i) {
            View view = taskLayout.getChildAt(i);
            if (view.getTag() != null) {
                taskLayout.removeView(view);
            }
        }
    }

    public void submit(View view) {
        ArrayList<Task> tasks = Client.getInstance().tasks;
        if (tasks != null) {
            Task task = tasks.get(currentTask);
            ArrayList<String> postedAnswers = new ArrayList<>();

            switch (task.type) {

                case TASK_FILL:
                case TASK_DRAG:
                    new TaskEvaluate(this).execute(jsInterface.data, task.task_id + "", task.task_type_id + "", "10");
                    break;

                case TASK_CHOICE:
                    int index = -1;
                    for (int i = taskLayout.getChildCount() - 1; i >= 0; --i) {
                        View v = taskLayout.getChildAt(i);
                        if (v instanceof RadioGroup) {
                            RadioGroup radioGroup = (RadioGroup) v;

                            for (int j = 0; j < radioGroup.getChildCount(); ++j) {
                                if (((RadioButton) radioGroup.getChildAt(j)).isChecked()) {
                                    index = j;
                                    break;
                                }
                            }
                        }
                    }

                    new TaskEvaluate(this).execute("[\"" + task.answers.get(index) + "\"]", task.task_id + "", task.task_type_id + "", "10");
                    break;

                case TASK_INPUT:
                    new TaskEvaluate(this).execute("[\"" + inputEditText.getText().toString() + "\"]", task.task_id + "", task.task_type_id + "", "10");
                    break;

                case TASK_MULTI:
                    for (int i = taskLayout.getChildCount() - 1; i >= 0; --i) {
                        View v = taskLayout.getChildAt(i);
                        if (v instanceof CheckBox) {
                            CheckBox checkBox = (CheckBox) v;

                            if (checkBox.isChecked()) {
                                postedAnswers.add(checkBox.getText().toString());
                            }
                        }
                    }
                    new TaskEvaluate(this).execute(new JSONArray(postedAnswers).toString(), task.task_id + "", task.task_type_id + "", "10");
                    break;
            }
        }
    }

    public void nextTask(View view) {
        ArrayList<Task> tasks = Client.getInstance().tasks;
        if (tasks.size() > currentTask + 1) {
            ++currentTask;
            updateTasks(tasks);
        }
    }

    public void previousTask(View view) {
        ArrayList<Task> tasks = Client.getInstance().tasks;
        if (currentTask - 1 >= 0) {
            --currentTask;
            updateTasks(tasks);
        }
    }
}