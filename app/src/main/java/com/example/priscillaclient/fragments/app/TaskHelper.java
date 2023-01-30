package com.example.priscillaclient.fragments.app;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;

import com.example.priscillaclient.viewmodels.app.models.Task;

import java.util.ArrayList;

public final class TaskHelper {

    public static void initializeTaskChoice(Context context, LinearLayout taskLayout, boolean dark, ArrayList<String> answers) {
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setTag("CLEAR");

        for (String answer : answers) {
            RadioButton radioButton = new RadioButton(context);

            radioButton.setText(answer);
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            if (dark)
                radioButton.setTextColor(0xffffffff);

            CompoundButtonCompat.setButtonTintList(radioButton, ContextCompat.getColorStateList(context, com.google.android.material.R.color.design_default_color_secondary));
            radioGroup.addView(radioButton);
        }
        taskLayout.addView(radioGroup);
    }

    public static void initializeTaskMulti(Context context, LinearLayout taskLayout, boolean dark, ArrayList<String> answers) {
        for (String answer : answers) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setTag("CLEAR");

            checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            checkBox.setText(answer);

            if (dark)
                checkBox.setTextColor(0xffffffff);

            CompoundButtonCompat.setButtonTintList(checkBox, ContextCompat.getColorStateList(context, com.google.android.material.R.color.design_default_color_secondary));
            taskLayout.addView(checkBox);
        }
    }

    public static String initializeTaskDrag(Task task) {
        String html = "";
        if (task.fakes != null) {
            StringBuilder htmlBuilder = new StringBuilder("<hr>");
            for (String fake : task.fakes) {
                htmlBuilder.append("<button onclick=\"return add(this);\">").append(fake).append("</button>");
            }
            html = htmlBuilder.toString();
        }
        return html;
    }

    public static String initializeTaskOrder(Task task) {
        ArrayList<String> codes = task.codes;

        String content = "<hr><style>pre{display:inline-block;vertical-align:middle}</style>";
        content += "<div class=\"codes\">";
        StringBuilder contentBuilder = new StringBuilder(content);
        for (int i = 0; i < codes.size(); ++i) {
            contentBuilder.append("<span><button onclick=\"up(this)\" class=\"arrow-up\">&uarr;</button><button onclick=\"down(this)\" class=\"arrow-down\">&darr;</button><span class=\"code\">").append(codes.get(i)).append("</span><br></span>");
        }
        content = contentBuilder.toString();
        content += "</div>";

        return content;
    }
}
