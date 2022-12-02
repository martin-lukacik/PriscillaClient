package com.example.priscillaclient;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.priscillaclient.api.GetActiveLessons;
import com.example.priscillaclient.api.GetActiveTasks;
import com.example.priscillaclient.api.HttpResponse;
import com.example.priscillaclient.models.Lesson;
import com.example.priscillaclient.models.Task;
import com.google.android.material.navigation.NavigationView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity implements
        HttpResponse,
        NavigationView.OnNavigationItemSelectedListener,
        Html.ImageGetter {

    int courseId;
    int chapterId;

    int currentLesson = -1;
    int currentLessonId = -1;

    int currentTask = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

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


    boolean updateLayout = true;

    @Override
    public void onUpdate(Object response) {

        if (((ArrayList<?>) response).get(0) instanceof Lesson) {
            ArrayList<Lesson> lessons = (ArrayList<Lesson>) response;

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
        } else if (((ArrayList<?>) response).get(0) instanceof Task) {
            TextView taskContent = findViewById(R.id.taskContent);

            tasks = (ArrayList<Task>) response;

            String content = tasks.get(currentTask).content;

            taskContent.setText(Html.fromHtml(content, TaskActivity.this, null));
            taskContent.setMovementMethod(new ScrollingMovementMethod());

        }

        // TODO Collections.sort(agentDtoList, (o1, o2) -> o1.getCustomerCount() - o2.getCustomerCount());
    }

    ArrayList<Task> tasks = null;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.i("TEST", "PASS " + item.getItemId());
        return true;
    }

    public void nextTask(View view) {
        if (tasks.size() > currentTask + 1) {
            ++currentTask;
            onUpdate(tasks);
        }
    }

    public void previousTask(View view) {
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