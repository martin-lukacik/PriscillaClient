package com.example.priscillaclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.priscillaclient.api.GetActiveLessons;
import com.example.priscillaclient.api.GetActiveTasks;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.api.TaskEvaluate;
import com.example.priscillaclient.models.Lesson;
import com.example.priscillaclient.models.Task;
import com.example.priscillaclient.models.TaskEval;
import com.example.priscillaclient.models.TaskType;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity implements
        HttpResponse<Object>,
        NavigationView.OnNavigationItemSelectedListener,
        Html.ImageGetter {

    int courseId;
    int chapterId;

    int currentLesson = -1;
    int currentLessonId = -1;

    int currentTask = 0;

    boolean updateLayout = true;

    LinearLayout taskLayout;

    EditText taskContent;
    EditorFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        taskLayout = findViewById(R.id.taskLayout);
        taskContent = findViewById(R.id.taskContent);

        Intent intent = getIntent();

        courseId = intent.getIntExtra("course_id", -1);
        chapterId = intent.getIntExtra("chapter_id", -1);

        currentLesson = intent.getIntExtra("current_lesson", -1);
        currentLessonId = intent.getIntExtra("current_lesson_id", -1);
        currentTask = intent.getIntExtra("current_task", 0);

        new GetActiveLessons(this, chapterId).execute();

        if (currentLesson != -1 && currentLessonId != -1) {
            // TODO get tasks for this lesson

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

    WebView webView;

    public void updateTasks(ArrayList<Task> tasks) {
        clearTaskLayout();


        Task task = tasks.get(currentTask);

        // WEBVIEW TEST
        // TODO webview when reading task, otherwise edittext for interaction
        // TODO webview can maybe catch input events?
        if (webView != null) {
            taskLayout.removeView(webView);
        }
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        String css = ".ql-syntax{background-color:#dcdcdc;color:#000;overflow:visible}.myCode,.myParagraph{padding:1px;margin:1px;cursor:move;float:left}.CodeMirror{border:1px solid #eee;border:1px solid #000;height:auto}";
        webView.loadData(css, "text/css; charset=utf-8", "UTF-8");
        webView.loadData(task.content, "text/html; charset=utf-8", "UTF-8");
        taskLayout.addView(webView, 0);
        // WEBVIEW TEST END

        Log.i("CONTENT", task.content.replaceAll("<pre class=\"ql-syntax\" spellcheck=\"false\">", "<blockquote style=\"color:red\">").replaceAll("</pre>", "</blockquote>"));

        taskContent.setText(Html.fromHtml(task.content.replaceAll("<pre class=\"ql-syntax\" spellcheck=\"false\">", "<font face=\"monospace\">").replaceAll("</pre>", "</blockquote>").replaceAll("&nbsp;", "\t"), TaskActivity.this, null));
        taskContent.setMovementMethod(new ScrollingMovementMethod());

        if (task.type == TaskType.TASK_CHOICE) {
            if (task.answers != null) {
                LinearLayout taskLayout = findViewById(R.id.taskLayout);
                RadioGroup radioGroup = new RadioGroup(this);
                radioGroup.setTag("CLEAR");

                for (String answer : task.answers) {
                    RadioButton checkBox = new RadioButton(this);
                    checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    checkBox.setText(answer);
                    radioGroup.addView(checkBox);
                }
                taskLayout.addView(radioGroup);
            }
        }

        if (task.type == TaskType.TASK_FILL) {
            // TODO compare taskContent to task.content
            // TODO extract differences around positions of EVERY start and end question mark
            task.content = task.content.replaceAll("§§_§§", "|███|");
            taskContent.setFocusableInTouchMode(true);
            taskContent.setText(Html.fromHtml(task.content, TaskActivity.this, null));
            taskContent.setMovementMethod(new ScrollingMovementMethod());

            filter = new EditorFilter(task);
            taskContent.setFilters(new InputFilter[] { filter });
        }
    }

    @Override
    public void onUpdate(Object response) {
        try {
            if (((ArrayList<?>) response).get(0) instanceof Lesson)
                updateLessons((ArrayList<Lesson>) response);
            else if (((ArrayList<?>) response).get(0) instanceof Task)
                updateTasks((ArrayList<Task>) response);
        } catch (Exception ignore) {
            TaskEval taskEval = (TaskEval) response;
            Toast.makeText(this, "Rating: " + taskEval.rating, Toast.LENGTH_SHORT).show();
        }
    }

    public void clearTaskLayout() {

        taskContent.setFocusable(false);
        taskContent.setFilters(new InputFilter[] { });

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
            filter.clear(taskContent.getText());
            if (task.type == TaskType.TASK_FILL) {
                ArrayList<String> userAnswers = new ArrayList<>();
                for (int i = 0; i < filter.startPositions.size(); ++i) {
                    int start = filter.startPositions.get(i);
                    int end = filter.endPositions.get(i);

                    userAnswers.add(taskContent.getText().toString().substring(start + 1, end));

                }

                new TaskEvaluate(this).execute(new JSONArray(userAnswers).toString(), task.task_id + "", task.task_type_id + "", "10");
            }

            if (task.type == TaskType.TASK_CHOICE) {
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
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.i("TEST", "PASS " + item.getItemId());
        return true;
    }

    public void nextTask(View view) {
        ArrayList<Task> tasks = Client.getInstance().tasks;
        if (tasks.size() > currentTask + 1) {
            ++currentTask;
            onUpdate(tasks);
        }
    }

    public void previousTask(View view) {
        ArrayList<Task> tasks = Client.getInstance().tasks;
        if (currentTask - 1 >= 0) {
            --currentTask;
            onUpdate(tasks);
        }
    }

    Drawable empty;
    @Override
    public Drawable getDrawable(String s) {/*
        LevelListDrawable d = new LevelListDrawable();
        empty = getResources().getDrawable(R.drawable.profile_icon);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
        new LoadImage().execute(s, d);*/
        s = s.replace("data:image/png;base64,", "");
        byte[] data = Base64.decode(s, Base64.DEFAULT);
        Bitmap d = BitmapFactory.decodeByteArray(data, 0, data.length);
        BitmapDrawable dr = new BitmapDrawable(getResources(), d);
        dr.setBounds(0, 0, d.getWidth(), d.getHeight());
        return dr;
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {
        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d("TAG", "doInBackground " + source);

            source = source.replace("data:image/png;base64,", "");
            try {


                byte[] data = Base64.decode(source, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                return bitmap;

                /*InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                //mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                mDrawable.setLevel(1);
                TextView textView = findViewById(R.id.taskContent);
                CharSequence t = textView.getText();
                textView.setText(t);
            }
        }
    }
}