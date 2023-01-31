package com.example.priscillaclient.fragments.app;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.TypedValue;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;

import com.example.priscillaclient.R;
import com.example.priscillaclient.viewmodels.app.models.Task;

import java.util.ArrayList;

public final class TaskHelper {

    public static void initializeTaskChoice(Context context, LinearLayout taskLayout, ArrayList<String> answers) {
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setTag("CLEAR");

        for (String answer : answers) {
            RadioButton radioButton = new RadioButton(context);

            radioButton.setText(answer);
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true);
            int color = typedValue.data;
            CompoundButtonCompat.setButtonTintList(radioButton, ColorStateList.valueOf(color));

            radioGroup.addView(radioButton);
        }
        taskLayout.addView(radioGroup);
    }

    public static void initializeTaskMulti(Context context, LinearLayout taskLayout, ArrayList<String> answers) {
        for (String answer : answers) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setTag("CLEAR");

            checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            checkBox.setText(answer);

            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true);
            int color = typedValue.data;
            CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(color));

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
