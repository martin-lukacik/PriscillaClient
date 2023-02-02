package com.example.priscillaclient.fragments.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import com.example.priscillaclient.R;
import com.example.priscillaclient.api.tasks.DoEvaluateTask;
import com.example.priscillaclient.api.tasks.DoPassTask;
import com.example.priscillaclient.api.tasks.DoRunProgram;
import com.example.priscillaclient.fragments.FragmentBase;
import com.example.priscillaclient.misc.JavascriptInterface;
import com.example.priscillaclient.misc.LoadingDialog;
import com.example.priscillaclient.viewmodels.app.ChaptersViewModel;
import com.example.priscillaclient.viewmodels.app.LessonsViewModel;
import com.example.priscillaclient.viewmodels.app.TaskResultViewModel;
import com.example.priscillaclient.viewmodels.app.TasksViewModel;
import com.example.priscillaclient.viewmodels.app.models.Answer;
import com.example.priscillaclient.viewmodels.app.models.Chapter;
import com.example.priscillaclient.viewmodels.app.models.Lesson;
import com.example.priscillaclient.viewmodels.app.models.Task;
import com.example.priscillaclient.viewmodels.app.models.TaskResult;
import com.example.priscillaclient.viewmodels.user.UserViewModel;
import com.example.priscillaclient.viewmodels.user.models.User;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.github.rosemoe.sora.langs.java.JavaLanguage;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;

public class TaskFragment extends FragmentBase {

    public static final String ARG_COURSE_ID = "courseId";
    public static final String ARG_CHAPTER_ID = "chapterId";

    private int courseId;
    private int chapterId;
    private int currentTask;
    private int currentLessonId = 0;

    ArrayList<Lesson> lessons;
    ArrayList<Task> tasks;

    UserViewModel userViewModel;
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
    CodeEditor codeEditor;

    LoadingDialog dialog;

    String css;
    String javascript;
    final JavascriptInterface javascriptInterface = new JavascriptInterface(getActivity());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = R.layout.fragment_task;

        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
            chapterId = getArguments().getInt(ARG_CHAPTER_ID);
        }

        dialog = new LoadingDialog(getActivity());

        lessonsViewModel = getViewModel(LessonsViewModel.class);
        taskResultViewModel = getViewModel(TaskResultViewModel.class);
        tasksViewModel = getViewModel(TasksViewModel.class);
        userViewModel = getViewModel(UserViewModel.class);

        lessonsViewModel.fetchData(chapterId);

        // Lessons
        lessonsViewModel.getData().observe(this, this::onUpdate);
        lessonsViewModel.getErrorState().observe(this, this::showError);

        // Task states
        tasksViewModel.getErrorState().observe(this, this::showError);
        tasksViewModel.getHelpState().observe(this, this::onUpdateHelp);
        tasksViewModel.getAnswerState().observe(this, this::onUpdateAnswer);

        // Task result
        taskResultViewModel.clear();
        taskResultViewModel.getData().observe(this, this::onUpdate);
        taskResultViewModel.getErrorState().observe(this, this::showError);
        taskResultViewModel.getLoadingState().observe(this, (isLoading) -> {
            if (dialog != null) {
                if (isLoading)
                    dialog.show();
                else
                    dialog.dismiss();
            }
        });
        taskResultViewModel.getSaveState().observe(this, this::showError);
        taskResultViewModel.getCodeLoadedState().observe(this, (data) -> {
            if (data != null && tasks != null && !tasks.isEmpty()) {
                codes = new ArrayList<>(data.y);
                onUpdate(tasks.get(currentTask));
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup views
        setupViews(savedInstanceState);
    }

    private void setupViews(Bundle savedInstanceState) {
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

        setupWebView();
        setupCodeEditor();

        buttonTaskHelp.setOnClickListener(this::getTaskHelp);
        buttonTaskNext.setOnClickListener(this::nextTask);
        buttonTaskPrevious.setOnClickListener(this::previousTask);
        buttonTaskSubmit.setOnClickListener(this::submit);

        boolean drawerStatus = true;
        if (savedInstanceState != null) {
            currentTask = savedInstanceState.getInt("currentTask");
            drawerStatus = savedInstanceState.getBoolean("drawerStatus");
        }

        if (drawerStatus) {
            DrawerLayout drawer = findViewById(R.id.drawerLayout);
            drawer.open();
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        outState.putBoolean("drawerStatus", drawer.isOpen());
        outState.putInt("currentTask", currentTask); // TODO remember task AND lesson
    }

    private void onUpdate(ArrayList<Lesson> lessons) {
        if (lessons == null || lessons.isEmpty())
            return;

        currentLessonId = lessons.get(0).id;
        currentTask = 0;
        this.lessons = lessons;

        tasksViewModel.fetchData(courseId, chapterId, currentLessonId, false);

        ChaptersViewModel chaptersViewModel = getViewModel(ChaptersViewModel.class);
        ArrayList<Chapter> chapters = chaptersViewModel.getData().getValue();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.bringToFront();
        Menu menu = navigationView.getMenu();
        menu.clear();

        // Set menu title
        if (chapters != null && !chapters.isEmpty()) {
            for (Chapter c : chapters) {
                if (c.id == chapterId) {
                    menu.add(c.name);
                    break;
                }
            }
        } else {
            menu.add(R.string.lessons);
        }
        menu.getItem(0).setEnabled(false);

        // Set menu items
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
    private void setupWebView() {
        String darkCss = (isDarkModeEnabled() ? readFile(R.raw.task_style_dark) : "");
        css = "<style>" + readFile(R.raw.task_style) + darkCss + "</style>";
        javascript = "<script>" + readFile(R.raw.task_script) + "</script>";

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(javascriptInterface, "Android");
        webView.setOnLongClickListener(v -> true);
        webView.setLongClickable(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        String loadingContent = "<div style=\"display:block;width:99%;position:absolute;top:50%;text-align:center;font-size:36px\">" +  getString(R.string.loading) + "</div>";
        String html = css + javascript + "<div id=\"task-content\">" + loadingContent + "</div>";
        webView.loadDataWithBaseURL(null, html, "text/html; charset=utf-8", "UTF-8", null);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Task list needs webView to be ready
                tasksViewModel.getData().observe(getViewLifecycleOwner(), (tasks) -> onUpdateTasks(tasks));
            }
        });
    }

    private void setupCodeEditor() {
        codeEditor = new CodeEditor(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 36, 0, 0);
        codeEditor.setLayoutParams(params);
        codeEditor.setHighlightCurrentLine(true);
        codeEditor.setHighlightBracketPair(true);
        codeEditor.setEditorLanguage(new JavaLanguage());
        codeEditor.setTypefaceText(Typeface.MONOSPACE);

        EditorColorScheme scheme = new EditorColorScheme(isDarkModeEnabled()) { };
        scheme.setColor(EditorColorScheme.WHOLE_BACKGROUND, isDarkModeEnabled() ? 0xFF333333 : 0xffffffff);
        scheme.setColor(EditorColorScheme.TEXT_NORMAL, isDarkModeEnabled() ? 0xffffffff : 0xFF333333);
        scheme.setColor(EditorColorScheme.LINE_NUMBER_BACKGROUND, isDarkModeEnabled() ? 0xFF333333 : 0xffffffff);
        scheme.setColor(EditorColorScheme.LINE_NUMBER, isDarkModeEnabled() ? 0xfff1f1f1 : 0xFF333333);
        scheme.setColor(EditorColorScheme.LINE_NUMBER_CURRENT, isDarkModeEnabled() ? 0xffffffff : 0xFF333333);
        codeEditor.setColorScheme(scheme);

        codeEditor.setOnTouchListener((v, event) -> {
            if (codeEditor.hasFocus()) {
                int eventMask = (event.getAction() & MotionEvent.ACTION_MASK);
                v.getParent().requestDisallowInterceptTouchEvent(true);
                if (eventMask == MotionEvent.ACTION_SCROLL) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
                }
            }
            return false;
        });
    }

    private void getTaskHelp(View view) {

        if (tasks.isEmpty())
            return;

        Task task = tasks.get(currentTask);

        int priceHelp = 10;
        int priceAnswer = 20;

        if (task.type == Task.Type.TASK_CODE
            || task.type == Task.Type.TASK_CODE2
            || task.type == Task.Type.TASK_CODE3) {
            priceHelp = 20;
            priceAnswer = 40;
        }

        String help = getString(R.string.help);
        String answer = getString(R.string.answer);
        String balance = getString(R.string.balance);
        String coins = getString(R.string.coins).toLowerCase(Locale.ROOT);

        User user = userViewModel.getData().getValue();

        int currentBalance = (user != null ? user.performance.coins : 0);

        String message =
                help + ": " +
                "<b>" + priceHelp + " " + coins + "</b>" +
                "<br><br>" +
                answer + ": " +
                "<b>" + priceAnswer + " " + coins + "</b>" +
                "<br><br><br>" +
                balance + ": " +
                "<b>" + currentBalance+ " " + coins + "</b></b>";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.help);
        builder.setMessage(Html.fromHtml(message));

        builder.setPositiveButton(R.string.help, (dialog, id) -> tasksViewModel.getHelp(task.task_id));
        builder.setNegativeButton(R.string.answer, (dialog, id) -> tasksViewModel.getAnswer(task.task_id));
        builder.setNeutralButton(R.string.cancel, null);

        Dialog d = builder.create();
        d.show();
    }

    boolean refreshTask = false;
    public void onUpdate(TaskResult result) {

        if (result == null)
            return;

        refreshTask = true;

        tasksViewModel.fetchData(courseId, chapterId, currentLessonId, true);
        showRatingDialog(result);

        userViewModel.fetchData();
    }

    public void onUpdateAnswer(Answer answer) {

        if (answer == null)
            return;


        if (tasks.isEmpty())
            return;

        ArrayList<String> answerList = answer.getAnswerList();
        Task task = tasks.get(currentTask);

        if (answerList.size() > 0) {
            switch (task.type) {
                case TASK_CODE:
                case TASK_CODE2:
                case TASK_CODE3:
                    break;

                case TASK_ORDER:
                    webView.evaluateJavascript("loadTaskOrder('" + task.content.replaceAll("\n", "<br>") + "', '" + new JSONArray(answerList) + "')", null);
                    break;

                case TASK_INPUT:
                    inputEditText.setText(answerList.get(0));
                    break;

                case TASK_CHOICE:
                    RadioGroup radioGroup = null;

                    for (int i = 0; i < taskLayout.getChildCount(); ++i) {
                        if (taskLayout.getChildAt(i) instanceof RadioGroup) {
                            radioGroup = (RadioGroup) taskLayout.getChildAt(i);
                        }
                    }

                    if (radioGroup != null) {
                        for (int i = 0; i < radioGroup.getChildCount(); ++i) {
                            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                            if (answerList.get(0).equals(radioButton.getText().toString())) {
                                radioButton.setChecked(true);
                                break;
                            }
                        }
                    }
                    break;

                case TASK_MULTI:
                    for (int i = 0; i < taskLayout.getChildCount(); ++i) {
                        if (taskLayout.getChildAt(i) instanceof CheckBox) {
                            CheckBox checkBox = (CheckBox) taskLayout.getChildAt(i);

                            for (String a : answerList) {
                                if (checkBox.getText().toString().equals(a)) {
                                    checkBox.setChecked(true);
                                }
                            }
                        }
                    }
                    break;

                case TASK_FILL:
                    webView.evaluateJavascript("loadTaskFill('" + new JSONArray(answerList) + "')", null);
                    break;

                case TASK_DRAG:
                    webView.evaluateJavascript("loadTaskDrag('" + new JSONArray(answerList) + "')", null);
                    break;
            }
        }

        // update coins, xp, etc.
        userViewModel.fetchData();
    }

    public void onUpdateHelp(Answer help) {
        if (help == null)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(help.answer);
        builder.setTitle(R.string.help);
        builder.setPositiveButton(R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // update coins, xp, etc.
        userViewModel.fetchData();
    }

    public void showRatingDialog(TaskResult eval) {

        View view = View.inflate(getActivity(), R.layout.layout_dialog_taskeval, null);

        LinearLayout stars = view.findViewById(R.id.dialog_stars);
        TextView compilation = view.findViewById(R.id.dialogCompilation);
        TextView execution = view.findViewById(R.id.dialogExecution);

        boolean limitScrollView = false;
        if (eval.execution != null && !eval.execution.isEmpty()) {
            execution.setText(getString(R.string.execution) + "\n\n" + eval.execution);
            execution.setVisibility(View.VISIBLE);
            limitScrollView = true;
        }
        if (eval.compilation != null && !eval.compilation.isEmpty()) {
            compilation.setText(getString(R.string.compilation) + "\n\n" + eval.compilation);
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

        Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        dialog.setContentView(view);
        view.setBackgroundResource(android.R.color.transparent);
        dialog.show();
        view.findViewById(R.id.dialog_dismiss).setOnClickListener(e -> dialog.dismiss());
    }

    private void setButtonsVisibility(Task task) {
        if (task.max_score == 0 && task.passed != 1) {
            buttonTaskHelp.setEnabled(false);
            buttonTaskHelp.setVisibility(View.INVISIBLE);
        } else {
            buttonTaskHelp.setEnabled(true);
            buttonTaskHelp.setVisibility(View.VISIBLE);
        }

        if (task.type != Task.Type.TASK_READ || task.passed == 0) {
            buttonTaskSubmit.setEnabled(true);
            buttonTaskSubmit.setVisibility(View.VISIBLE);
        } else {
            buttonTaskSubmit.setEnabled(false);
            buttonTaskSubmit.setVisibility(View.INVISIBLE);
        }

        if (task.passed == 1 && task.max_score == 0) {
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

    @SuppressLint("ClickableViewAccessibility")
    public void onUpdate(Task task) {

        if (codes.isEmpty()) {
            if (task.files == null)
                return; // TODO ??
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

            fileNameView.setPadding(25, 0, 0, 0);
            fileNameView.setOnClickListener((e) -> {
                codes.set(currentFileIndex, codeEditor.getText().toString());
                currentFileIndex = finalI;
                codeEditor.setText(codes.get(finalI));

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

        codeEditor.setText(codes.get(0));
        codeTaskLayout.addView(codeEditor);
    }

    int currentFileIndex = 0;
    public void onUpdateTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;

        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (tasks.isEmpty())
            return;

        Task task = tasks.get(currentTask);
        stars.removeAllViews();
        setButtonsVisibility(task);

        if (task.passed == 1 && task.max_score > 0)
            setStarsRating(task);

        if (refreshTask) {
            refreshTask = false;
            return;
        }

        clearTaskLayout();

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
                    TaskHelper.initializeTaskChoice(getActivity(), taskLayout, task.answers);
                break;

            case TASK_FILL:
                content = task.content.replaceAll("§§_§§", "<input class=\"answer\" type=\"text\" oninput=\"process();\">");
                break;

            case TASK_INPUT:
                inputEditText.setVisibility(View.VISIBLE);
                break;

            case TASK_MULTI:
                if (task.answers != null)
                    TaskHelper.initializeTaskMulti(getActivity(), taskLayout, task.answers);
                break;

            case TASK_DRAG:
                String dragInputHtml = "<span onclick=\"return remove(this);\" class=\"drag\">&nbsp;</span>";
                content = task.content.replaceAll("§§_§§", dragInputHtml);
                content += TaskHelper.initializeTaskDrag(task);
                break;

            case TASK_ORDER:
                content += TaskHelper.initializeTaskOrder(task);
                break;
        }

        String finalContent = content.replaceAll("\n", "<br>").replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'");
        webView.evaluateJavascript("loadData('" + finalContent + "')", s -> {
            ScrollView scrollView = findViewById(R.id.taskScrollView);
            scrollView.scrollTo(0, 0);
        });

        //webView.evaluateJavascript("loadData('<p>Priraďte správny vysvetľujúci komentár:</p><p>LF §§_§§</p><p>CR §§_§§</p><p>form feed §§_§§</p><p>BEL §§_§§</p>')", null);

        taskLayout.setVisibility(View.VISIBLE);
    }

    private void clearTaskLayout() {

        Timer timer = new Timer();
        if (timerTask != null) {
            currentTaskClock = 0;
            timerTask.cancel();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                ++currentTaskClock;
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);


        taskLayout.setVisibility(View.INVISIBLE);

        String str = (currentTask + 1) + " / " + tasks.size();
        taskCount.setText(str);

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

    TimerTask timerTask;
    int currentTaskClock = 0;
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
                    codes.set(currentFileIndex, codeEditor.getText().toString());
                    taskResultViewModel.saveCode(task.task_id, task.fileNames, codes);
                    taskResultViewModel.postData(new DoRunProgram(exeType, task, task.fileNames, codes, currentTaskClock));
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

            taskResultViewModel.postData(new DoEvaluateTask(task, answer, currentTaskClock));
        }
    }

    public void nextTask(View view) {
        if (tasks.size() > currentTask + 1) {
            ++currentTask;
            onUpdateTasks(tasks);
        }
    }

    public void previousTask(View view) {
        if (currentTask - 1 >= 0) {
            --currentTask;
            onUpdateTasks(tasks);
        }
    }
}