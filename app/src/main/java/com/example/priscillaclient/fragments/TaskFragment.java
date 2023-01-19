package com.example.priscillaclient.fragments;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.priscillaclient.R;
import com.example.priscillaclient.TaskViewInterface;
import com.example.priscillaclient.api.GetActiveLessons;
import com.example.priscillaclient.api.GetActiveTasks;
import com.example.priscillaclient.api.TaskEvaluate;
import com.example.priscillaclient.client.Client;
import com.example.priscillaclient.models.Lesson;
import com.example.priscillaclient.models.Task;
import com.example.priscillaclient.models.TaskEval;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends FragmentBase {

    private static final String ARG_COURSE_ID = "courseId";
    private static final String ARG_CHAPTER_ID = "chapterId";

    private int courseId;
    private int chapterId;
    private int currentLesson = -1;
    private int lessonId = -1;
    private int currentTask = 0;

    // ==============================================================================

    Button buttonTaskNext;
    Button buttonTaskPrevious;
    Button buttonTaskHelp;
    Button buttonTaskSubmit;
    LinearLayout taskLayout;
    WebView webView;
    EditText inputEditText;

    TaskViewInterface jsInterface = new TaskViewInterface(getActivity());

    public TaskFragment() { }

    public static TaskFragment newInstance(int courseId, int chapterId) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COURSE_ID, courseId);
        args.putInt(ARG_CHAPTER_ID, chapterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
            chapterId = getArguments().getInt(ARG_CHAPTER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new GetActiveLessons(this, chapterId).execute();

        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void createLayout() {

        taskLayout = findViewById(R.id.taskLayout);
        inputEditText = findViewById(R.id.inputEditText);
        inputEditText.setVisibility(View.GONE);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(jsInterface, "Android");

        buttonTaskNext = findViewById(R.id.buttonTaskNext);
        buttonTaskPrevious = findViewById(R.id.buttonTaskPrevious);
        buttonTaskHelp = findViewById(R.id.buttonTaskHelp);
        buttonTaskSubmit = findViewById(R.id.buttonTaskSubmit);

        buttonTaskNext.setOnClickListener(this::nextTask);
        buttonTaskPrevious.setOnClickListener(this::previousTask);
        buttonTaskSubmit.setOnClickListener(this::submit);
    }

    @Override
    public void onUpdate(Object response) {

        if (taskLayout == null) {
            createLayout();
        }

        Client client = Client.getInstance();

        if (response.equals(client.lessons)) {
            int id = lessonId == -1 ? client.lessons.get(0).id : lessonId;
            new GetActiveTasks(this, id).execute();

            updateLessonList(client.lessons);
        } else if (response.equals(client.tasks)) {
            currentTask = 0;
            updateTaskList(client.tasks);
        } else if (response instanceof TaskEval) {
            Toast.makeText(getActivity(), "Rating: " + ((TaskEval) response).rating, Toast.LENGTH_SHORT).show();
        }
    }

    public void updateLessonList(ArrayList<Lesson> lessons) {
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
                lessonId = lesson.id;
                updateLessonList(lessons);
                DrawerLayout drawer = findViewById(R.id.drawerLayout);
                drawer.closeDrawers();
                return false;
            });
        }

        navigationView.invalidate();
    }

    public void updateTaskList(ArrayList<Task> tasks) {
        clearTaskLayout();

        Task task = tasks.get(currentTask);

        String css = "<style>" + readFile(R.raw.task_style) + "</style>";
        String javascript = "<script>" + readFile(R.raw.task_script) + "</script>";

        String content = task.content;

        switch (task.type) {
            case TASK_CHOICE:
                if (task.answers != null) {
                    RadioGroup radioGroup = new RadioGroup(getActivity());
                    radioGroup.setTag("CLEAR");

                    for (String answer : task.answers) {
                        RadioButton radioButton = new RadioButton(getActivity());
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
                        CheckBox checkBox = new CheckBox(getActivity());
                        checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        checkBox.setText(answer);
                        checkBox.setTag("CLEAR");
                        taskLayout.addView(checkBox);
                    }
                }
                break;
            case TASK_DRAG:

                content = task.content.replaceAll("§§_§§", "<span onclick=\"return remove(this);\" class=\"drag\" style=\"text-align:center; color:white; display:inline-block; background:black; width:4em\"></span>");

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

    private void clearTaskLayout() {
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
            updateTaskList(tasks);
        }
    }

    public void previousTask(View view) {
        ArrayList<Task> tasks = Client.getInstance().tasks;
        if (currentTask - 1 >= 0) {
            --currentTask;
            updateTaskList(tasks);
        }
    }
}