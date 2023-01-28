package com.example.priscillaclient.fragments.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.tasks.DoEvaluateTask;
import com.example.priscillaclient.api.tasks.DoPassTask;
import com.example.priscillaclient.api.tasks.DoRunProgram;
import com.example.priscillaclient.viewmodels.app.LessonsViewModel;
import com.example.priscillaclient.viewmodels.app.TaskResultViewModel;
import com.example.priscillaclient.viewmodels.app.TasksViewModel;
import com.example.priscillaclient.viewmodels.app.models.Lesson;
import com.example.priscillaclient.viewmodels.app.models.Task;
import com.example.priscillaclient.viewmodels.app.models.TaskResult;
import com.example.priscillaclient.viewmodels.app.models.TaskType;
import com.example.priscillaclient.viewmodels.user.UserViewModel;
import com.example.priscillaclient.viewmodels.user.models.Theme;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.util.JavascriptInterface;
import com.example.priscillaclient.util.LoadingDialog;
import com.example.priscillaclient.util.TaskHelper;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;

import io.github.rosemoe.sora.langs.java.JavaLanguage;
import io.github.rosemoe.sora.widget.CodeEditor;

public class TaskFragment extends FragmentBase {

    private static final String ARG_COURSE_ID = "courseId";
    private static final String ARG_CHAPTER_ID = "chapterId";

    private int themeId = Theme.THEME_LIGHT;

    private int courseId;
    private int chapterId;
    private int currentTask = 0;
    private int currentLessonId = 0;

    ArrayList<Lesson> lessons;
    ArrayList<Task> tasks;

    LessonsViewModel lessonsViewModel;
    TasksViewModel tasksViewModel;
    TaskResultViewModel taskResultViewModel;

    Button buttonTaskNext;
    Button buttonTaskPrevious;
    Button buttonTaskHelp;
    Button buttonTaskSubmit;
    LinearLayout taskLayout;
    LinearLayout codeTaskLayout;
    TextView taskCount;
    WebView webView;
    EditText inputEditText;
    LinearLayout stars;

    JavascriptInterface javascriptInterface = new JavascriptInterface(getActivity());

    public TaskFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_task;

        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
            chapterId = getArguments().getInt(ARG_CHAPTER_ID);
        }

        taskResultViewModel = (TaskResultViewModel) getViewModel(TaskResultViewModel.class);
        taskResultViewModel.clear();
        taskResultViewModel.getData().observe(this, (data) -> {
            if (taskResultViewModel.hasError())
                showError(taskResultViewModel.getError());
            else
                onUpdate(data);
            if (dialog != null)
                dialog.dismiss();
        });
        taskResultViewModel.getSaveState().observe(this, (data) -> {
            if (taskResultViewModel.hasError())
                showError(taskResultViewModel.getError());
            else
                showError(data);
        });
        taskResultViewModel.getLoadedCode().observe(this, (data) -> {
            if (taskResultViewModel.hasError())
                showError(taskResultViewModel.getError());
            else if (tasks != null && !tasks.isEmpty()) {
                codes = new ArrayList<>(data.y);//tasks.get(currentTask).files = data.y;

                updateTaskCode(tasks.get(currentTask));
            }
        });

        tasksViewModel = (TasksViewModel) getViewModel(TasksViewModel.class);
        tasksViewModel.getData().observe(this, (data) -> {
            if (tasksViewModel.hasError())
                showError(tasksViewModel.getError());
            else
                onUpdateTasks(data);
        });

        lessonsViewModel = (LessonsViewModel) getViewModel(LessonsViewModel.class);
        lessonsViewModel.getData().observe(this, (data) -> {
            if (lessonsViewModel.hasError())
                showError(lessonsViewModel.getError());
            else
                onUpdateLessons(data);
        });
        lessonsViewModel.fetchData(chapterId);

        if (getActivity() != null) {
            SharedPreferences settings = getActivity().getSharedPreferences("settings", 0);
            themeId = settings.getInt("theme_id", 0);
        }
    }

    private void onUpdateTasks(ArrayList<Task> tasks) {

        this.tasks = tasks;

        updateTaskList(tasks);
    }

    private void onUpdateLessons(ArrayList<Lesson> lessons) {

        if (lessons.isEmpty())
            return;

        currentLessonId = lessons.get(0).id;
        currentTask = 0;
        this.lessons = lessons;

        tasksViewModel.fetchData(courseId, chapterId, currentLessonId, false);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.bringToFront();
        Menu menu = navigationView.getMenu();
        menu.clear();
        menu.add(R.string.lessons);
        menu.getItem(0).setEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        drawer.open();

        boolean initialChecked = false;
        for (Lesson lesson : lessons) {
            MenuItem menuItem = menu.add(lesson.name);

            if (!initialChecked) {
                menuItem.setChecked(initialChecked = true);
            }

            menuItem.setOnMenuItemClickListener((item) -> onSelectLesson(item, lesson.id));
        }

        navigationView.invalidate();
    }

    private boolean onSelectLesson(MenuItem item, int lessonId) {
        NavigationView navigationView = findViewById(R.id.navigationView);
        Menu menu = navigationView.getMenu();

        for (int i = 0; i < menu.size(); ++i) {
            menu.getItem(i).setChecked(false);
        }

        currentTask = 0;
        item.setChecked(true);
        currentLessonId = lessonId;
        tasksViewModel.fetchData(courseId, chapterId, lessonId, false);

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        drawer.closeDrawers();
        return true;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = findViewById(R.id.webView);
        stars = findViewById(R.id.stars);
        taskLayout = findViewById(R.id.taskLayout);
        codeTaskLayout = findViewById(R.id.codeTaskLayout);
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
        webView.setOnLongClickListener(v -> true);
        webView.setLongClickable(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        buttonTaskHelp.setOnClickListener(this::getTaskHelp);
        buttonTaskNext.setOnClickListener(this::nextTask);
        buttonTaskPrevious.setOnClickListener(this::previousTask);
        buttonTaskSubmit.setOnClickListener(this::submit);
    }

    private void getTaskHelp(View view) {
        // TODO Implement help
    }

    boolean refreshTask = false;
    public void onUpdate(Object response) {

        refreshTask = true;

        tasksViewModel.fetchData(courseId, chapterId, currentLessonId, true);

        if (response instanceof TaskResult) {
            showRatingDialog(((TaskResult) response));

            UserViewModel userViewModel = ViewModelProviders.of(requireActivity()).get(UserViewModel.class);
            userViewModel.fetchData();
        }
    }

    public void showRatingDialog(TaskResult eval) {

        Dialog dialog = new Dialog(getActivity());

        View view = View.inflate(getActivity(), R.layout.layout_dialog_taskeval, null);

        LinearLayout stars = view.findViewById(R.id.dialog_stars);
        TextView compilation = view.findViewById(R.id.dialogCompilation);
        TextView execution = view.findViewById(R.id.dialogExecution);

        boolean limitScrollView = false;
        if (eval.execution != null && !eval.execution.isEmpty()) {
            execution.setText("Execution:\n\n" + eval.execution);
            execution.setVisibility(View.VISIBLE);
            limitScrollView = true;
        }
        if (eval.compilation != null && !eval.compilation.isEmpty()) {
            compilation.setText("Compilation:\n\n" + eval.compilation);
            compilation.setVisibility(View.VISIBLE);
            limitScrollView = true;
        }
        if (limitScrollView) {
            ScrollView scrollView = view.findViewById(R.id.dialogScrollView);
            scrollView.getLayoutParams().height = 500;
            scrollView.requestLayout();
        }

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

    private void setButtonsVisibility(Task task) {
        if (task.max_score == 0 && task.passed != 1) {
            buttonTaskHelp.setVisibility(View.GONE);
        } else {
            buttonTaskHelp.setVisibility(View.VISIBLE);
        }

        if (task.type != TaskType.TASK_READ || task.passed == 0) {
            buttonTaskSubmit.setVisibility(View.VISIBLE);
        } else {
            buttonTaskSubmit.setVisibility(View.GONE);
        }

        if (task.passed == 1) {
            buttonTaskHelp.setText(R.string.done);
            buttonTaskHelp.setEnabled(false);
        } else {
            buttonTaskHelp.setText(R.string.help);
            buttonTaskHelp.setEnabled(true);
        }
    }

    private void setStarsRating(Task task) {
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
    CodeEditor codeView;
    @SuppressLint("ClickableViewAccessibility")
    public void updateTaskCode(Task task) {

        codeView = new CodeEditor(getActivity());
        codeView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        codeView.setHighlightCurrentLine(true);
        codeView.setHighlightBracketPair(true);
        codeView.setEditorLanguage(new JavaLanguage());
        codeView.setTypefaceText(Typeface.MONOSPACE);

        codeView.setOnTouchListener((v, event) -> {
            if (codeView.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                if ((event.getAction() & MotionEvent.ACTION_MASK)
                    == MotionEvent.ACTION_SCROLL) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });

        if (codes.isEmpty()) {
            codes = new ArrayList<>(task.files);
        }

        for (int i = 0; i < task.fileNames.size(); ++i) {
            TextView fileNameView = new TextView(getActivity());
            fileNameView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            fileNameView.setText(task.fileNames.get(i));
            fileNameView.setTextSize(18);


            int finalI = i;
            int defaultColor = fileNameView.getTextColors().getDefaultColor();
            if (i == 0) {
                fileNameView.setTextColor(0xff008000);
            }

            fileNameView.setOnClickListener((e) -> {
                codes.set(currentIndex, codeView.getText().toString());
                currentIndex = finalI;
                codeView.setText(codes.get(finalI));

                for (int j = 0; j < codeTaskLayout.getChildCount(); ++j) {
                    View v = codeTaskLayout.getChildAt(j);
                    if (v instanceof TextView) {
                        ((TextView) v).setTextColor(defaultColor);
                    }
                }

                ((TextView) e).setTextColor(0xff008000);
            });

            codeTaskLayout.addView(fileNameView);
        }

        codeView.setText(codes.get(0));
        codeTaskLayout.addView(codeView);
    }

    int currentIndex = 0;
    public void updateTaskList(ArrayList<Task> tasks) {

        if (tasks.isEmpty())
            return;

        Task task = tasks.get(currentTask);
        clearTaskLayout();
        setButtonsVisibility(task);

        if (task.passed == 1 && task.max_score > 0)
            setStarsRating(task);

        int taskStyleId = R.raw.task_style;

        if (themeId == 2)
            taskStyleId = R.raw.task_style_dark;

        String css = "<style>" + readFile(taskStyleId) + "</style>";
        String javascript = "<script>" + readFile(R.raw.task_script) + "</script>";

        String content = task.content;


        codeTaskLayout.setVisibility(View.GONE);
        switch (task.type) {

            case TASK_CODE:
            case TASK_CODE2:
            case TASK_CODE3:

                taskResultViewModel.loadCode(task.task_id);
                codeTaskLayout.setVisibility(View.VISIBLE);

                break;

            case TASK_CHOICE:
                if (task.answers != null)
                    TaskHelper.initializeRadioGroup(getActivity(), taskLayout, themeId, task.answers);
                break;
            case TASK_FILL:
                content = task.content.replaceAll("§§_§§", "<input class=\"answer\" type=\"text\" oninput=\"process();\">");
                break;
            case TASK_INPUT:
                inputEditText.setVisibility(View.VISIBLE);
                break;
            case TASK_MULTI:
                if (task.answers != null)
                    TaskHelper.initializeCheckBoxes(getActivity(), taskLayout, themeId, task.answers);
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

        webView.loadDataWithBaseURL(null, css + javascript + content, "text/html; charset=utf-8", "UTF-8", null);

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

        webView.setVisibility(View.GONE);
        taskCount.setText((currentTask + 1) + " / " + tasks.size());
        stars.removeAllViews();

        codeTaskLayout.removeAllViews();
        codeTaskLayout.setVisibility(View.GONE);

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

    ArrayList<String> codes = new ArrayList<>();

    public void submit(View view) {
        if (!tasks.isEmpty()) {
            Task task = tasks.get(currentTask);
            ArrayList<String> postedAnswers = new ArrayList<>();

            int exeType = 0;
            String answer = null;
            switch (task.type) {

                case TASK_CODE3:
                    //exeType = 1;
                case TASK_CODE:
                case TASK_CODE2:
                    codes.set(currentIndex, codeView.getText().toString());
                    // TODO implement save button for code tasks only
                    taskResultViewModel.saveCode(task.task_id, task.fileNames, codes);
                    dialog = new LoadingDialog(getActivity());
                    dialog.show();
                    taskResultViewModel.postData(new DoRunProgram(exeType, task, task.fileNames, codes, 60));
                    return;

                case TASK_READ:

                    taskResultViewModel.postData(new DoPassTask(task));
                    return;

                case TASK_ORDER:
                case TASK_FILL:
                case TASK_DRAG:
                    answer = javascriptInterface.data;
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

                    answer = "[\"" + (index == -1 ? "" : task.answers.get(index)) + "\"]";
                    break;

                case TASK_INPUT:
                    answer = "[\"" + inputEditText.getText().toString() + "\"]";
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
                    answer = new JSONArray(postedAnswers).toString();
                    break;
            }

            taskResultViewModel.postData(new DoEvaluateTask(task, answer, 10));
        }
    }

    public void nextTask(View view) {
        if (tasks.size() > currentTask + 1) {
            ++currentTask;
            updateTaskList(tasks);
        }
    }

    public void previousTask(View view) {
        if (currentTask - 1 >= 0) {
            --currentTask;
            updateTaskList(tasks);
        }
    }
}