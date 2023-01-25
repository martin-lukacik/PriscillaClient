package com.example.priscillaclient.views.fragments.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.priscillaclient.MainActivity;
import com.example.priscillaclient.R;
import com.example.priscillaclient.api.app.EvaluateTask;
import com.example.priscillaclient.api.app.GetLessons;
import com.example.priscillaclient.api.app.GetTasks;
import com.example.priscillaclient.api.app.SetPassedTask;
import com.example.priscillaclient.api.user.GetUserParams;
import com.example.priscillaclient.models.Client;
import com.example.priscillaclient.models.Lesson;
import com.example.priscillaclient.models.Task;
import com.example.priscillaclient.models.TaskResult;
import com.example.priscillaclient.models.TaskType;
import com.example.priscillaclient.views.JavascriptInterface;
import com.example.priscillaclient.views.fragments.FragmentBase;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;

public class TaskFragment extends FragmentBase {

    private static final String ARG_CHAPTER_ID = "chapterId";

    private int chapterId;
    private int lessonId = -1;
    private int currentTask = 0;

    Button buttonTaskNext;
    Button buttonTaskPrevious;
    Button buttonTaskHelp;
    Button buttonTaskSubmit;
    LinearLayout taskLayout;
    TextView taskCount;
    WebView webView;
    EditText inputEditText;
    LinearLayout stars;

    JavascriptInterface javascriptInterface = new JavascriptInterface(getActivity());

    public TaskFragment() { }

    public static TaskFragment newInstance(int chapterId) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHAPTER_ID, chapterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_task;

        if (getArguments() != null) {
            chapterId = getArguments().getInt(ARG_CHAPTER_ID);
        }

        new GetLessons(this, chapterId).execute();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = findViewById(R.id.webView);
        stars = findViewById(R.id.stars);
        taskLayout = findViewById(R.id.taskLayout);
        taskCount = findViewById(R.id.taskCount);
        inputEditText = findViewById(R.id.inputEditText);
        buttonTaskNext = findViewById(R.id.buttonTaskNext);
        buttonTaskPrevious = findViewById(R.id.buttonTaskPrevious);
        buttonTaskHelp = findViewById(R.id.buttonTaskHelp);
        buttonTaskSubmit = findViewById(R.id.buttonTaskSubmit);

        inputEditText.setVisibility(View.GONE);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(javascriptInterface, "Android");

        buttonTaskHelp.setOnClickListener(this::getTaskHelp);
        buttonTaskNext.setOnClickListener(this::nextTask);
        buttonTaskPrevious.setOnClickListener(this::previousTask);
        buttonTaskSubmit.setOnClickListener(this::submit);
    }

    private void getTaskHelp(View view) {
        // TODO implement
    }

    boolean refreshTask = false;
    @Override
    public void onUpdate(Object response) {

        Client client = Client.getInstance();

        int id = lessonId == -1 ? client.lessons.get(0).id : lessonId;
        if (response.equals(client.lessons)) {
            currentTask = 0;
            new GetTasks(this, id).execute();
            DrawerLayout drawer = findViewById(R.id.drawerLayout);
            drawer.open();
            updateLessonList(client.lessons);
        } else if (response.equals(client.tasks)) {
            if (refreshTask) {
                for (int i = 0; i < client.tasks.size(); ++i) {
                    if (client.tasks.get(i).passed == 1) {
                        currentTask = i;
                        break;
                    }
                }
                refreshTask = false;
            }
            updateTaskList(client.tasks);
        } else if (response instanceof TaskResult || response instanceof String) {

            shouldResetLayout = false;
            client.courses.clear();
            client.chapters.clear();

            new GetTasks(this, id).execute();

            refreshTask = true;

            if (response instanceof TaskResult) {
                showRatingDialog(((TaskResult) response));
                new GetUserParams((MainActivity) getActivity()).execute();
            }
        }
    }

    public void showRatingDialog(TaskResult eval) {

        Dialog dialog = new Dialog(getActivity());

        View view = View.inflate(getActivity(), R.layout.layout_dialog_taskeval, null);

        LinearLayout stars = view.findViewById(R.id.dialog_stars);
        for (int i = 1; i <= 5; ++i) {
            ImageView star = new ImageView(getActivity());
            star.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            int ratingAdjusted = (int) Math.ceil(eval.rating / 20f);
            if (ratingAdjusted >= i) {
                star.setBackgroundResource(R.drawable.ic_star_full);
            } else {
                star.setBackgroundResource(R.drawable.ic_star);
            }

            stars.addView(star);
        }

        view.findViewById(R.id.dialog_dismiss).setOnClickListener(e -> dialog.dismiss());

        dialog.setCancelable(true);
        dialog.setContentView(view);
        dialog.show();
    }

    public void updateLessonList(ArrayList<Lesson> lessons) {
        NavigationView navigationView = findViewById(R.id.navigationView);
        Menu menu = navigationView.getMenu();
        menu.clear();

        menu.add(R.string.lessons);
        menu.getItem(0).setEnabled(false);

        navigationView.bringToFront();

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        boolean initialChecked = false;
        for (Lesson lesson : lessons) {
            MenuItem item = menu.add(lesson.name);

            if (!initialChecked) {
                item.setChecked(initialChecked = true);
            }

            item.setOnMenuItemClickListener((e) -> {
                for (int i = 0; i < menu.size(); ++i) {
                    menu.getItem(i).setChecked(false);
                }

                currentTask = 0;
                e.setChecked(true);
                lessonId = lesson.id;
                new GetTasks(this, lessonId).execute();
                drawer.closeDrawers();
                return false;
            });
        }

        navigationView.invalidate();
    }

    boolean shouldResetLayout = true;
    public void updateTaskList(ArrayList<Task> tasks) {

        if (!shouldResetLayout) {
            shouldResetLayout = true;
            return;
        }

        webView.setVisibility(View.GONE);

        clearTaskLayout();
        shouldResetLayout = true;

        taskCount.setText((currentTask + 1) + " / " + tasks.size());

        Task task = tasks.get(currentTask);

        stars.removeAllViews();
        if (task.max_score == 0 && task.passed != 1) {
            buttonTaskHelp.setVisibility(View.GONE);
        } else {
            buttonTaskHelp.setVisibility(View.VISIBLE);
        }

        if ((task.type == TaskType.TASK_READ && task.passed == 0) || (task.type != TaskType.TASK_READ)) {
            buttonTaskSubmit.setVisibility(View.VISIBLE);
        } else {
            buttonTaskSubmit.setVisibility(View.GONE);
        }

        if (task.passed == 1) {
            buttonTaskHelp.setText("PASSED");
            buttonTaskHelp.setEnabled(false);

            if (task.max_score > 0) {
                for (int i = 1; i <= 5; ++i) {
                    ImageView star = new ImageView(getActivity());
                    star.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                    int ratingAdjusted = (int) Math.ceil((task.score / (float) task.max_score) * 100 / 20f);
                    if (ratingAdjusted >= i) {
                        star.setBackgroundResource(R.drawable.ic_star_full);
                    } else {
                        star.setBackgroundResource(R.drawable.ic_star);
                    }

                    stars.addView(star);
                }
            }
        } else {
            buttonTaskHelp.setText("Help");
            buttonTaskHelp.setEnabled(true);
        }

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
            case TASK_ORDER:
                ArrayList<String> codes = task.codes;

                content += "<hr><style>pre{display:inline-block;vertical-align:middle}</style>";
                content += "<div class=\"codes\">";
                for (int i = 0; i < codes.size(); ++i) {
                    content += "<span><button onclick=\"up(this)\" class=\"arrow-up\">&uarr;</button><button onclick=\"down(this)\" class=\"arrow-down\">&darr;</button><span class=\"code\">" + codes.get(i) + "</span><br></span>";
                }
                content += "</div>";

                break;
        }

        webView.loadData(css + javascript + content, "text/html; charset=utf-8", "UTF-8");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    webView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void clearTaskLayout() {
        ArrayList<Task> tasks = Client.getInstance().tasks;

        inputEditText.setText("");
        inputEditText.setVisibility(View.GONE);
        for (int i = taskLayout.getChildCount() - 1; i >= 0; --i) {
            View view = taskLayout.getChildAt(i);
            if (view.getTag() != null) {
                taskLayout.removeView(view);
            }
        }

        if (currentTask > 0) {
            buttonTaskPrevious.setVisibility(View.VISIBLE);
        } else {
            buttonTaskPrevious.setVisibility(View.INVISIBLE);
        }

        if (currentTask + 1 >= tasks.size()) {
            buttonTaskNext.setVisibility(View.INVISIBLE);
        } else {
            buttonTaskNext.setVisibility(View.VISIBLE);
        }
    }

    public void submit(View view) {
        ArrayList<Task> tasks = Client.getInstance().tasks;

        if (!tasks.isEmpty()) {
            Task task = tasks.get(currentTask);
            ArrayList<String> postedAnswers = new ArrayList<>();

            switch (task.type) {

                case TASK_ORDER:
                case TASK_FILL:
                case TASK_DRAG:
                    new EvaluateTask(this).execute(javascriptInterface.data, task.task_id + "", task.task_type_id + "", "10");
                    break;

                case TASK_READ:
                    new SetPassedTask(this, task.task_id).execute();
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

                    String answer = (index == -1 ? "" : task.answers.get(index));
                    new EvaluateTask(this).execute("[\"" + answer + "\"]", task.task_id + "", task.task_type_id + "", "10");
                    break;

                case TASK_INPUT:
                    new EvaluateTask(this).execute("[\"" + inputEditText.getText().toString() + "\"]", task.task_id + "", task.task_type_id + "", "10");
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
                    new EvaluateTask(this).execute(new JSONArray(postedAnswers).toString(), task.task_id + "", task.task_type_id + "", "10");
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