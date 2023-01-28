package com.example.priscillaclient.util;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;

import java.util.ArrayList;

public final class TaskHelper {

    public static void initializeRadioGroup(Context context, LinearLayout taskLayout, int themeId, ArrayList<String> answers) {
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setTag("CLEAR");

        for (String answer : answers) {
            RadioButton radioButton = new RadioButton(context);

            radioButton.setText(answer);
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            if (themeId == 2)
                radioButton.setTextColor(0xffffffff);

            CompoundButtonCompat.setButtonTintList(radioButton, ContextCompat.getColorStateList(context, com.google.android.material.R.color.design_default_color_secondary));
            radioGroup.addView(radioButton);
        }
        taskLayout.addView(radioGroup);
    }

    public static void initializeCheckBoxes(Context context, LinearLayout taskLayout, int themeId, ArrayList<String> answers) {
        for (String answer : answers) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setTag("CLEAR");

            checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            checkBox.setText(answer);

            if (themeId == 2)
                checkBox.setTextColor(0xffffffff);

            CompoundButtonCompat.setButtonTintList(checkBox, ContextCompat.getColorStateList(context, com.google.android.material.R.color.design_default_color_secondary));
            taskLayout.addView(checkBox);
        }
    }
}
