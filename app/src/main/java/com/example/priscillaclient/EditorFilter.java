package com.example.priscillaclient;

import android.text.InputFilter;
import android.text.Spanned;

import com.example.priscillaclient.models.Task;

import java.util.ArrayList;

public class EditorFilter implements InputFilter {


    Task task;

    public EditorFilter(Task task) {
        super();
        this.task = task;
    }

    ArrayList<Integer> startPositions = new ArrayList<>();
    ArrayList<Integer> endPositions = new ArrayList<>();

    @Override
    public CharSequence filter(CharSequence str, int sstart, int send, Spanned dest, int start, int end) {

        startPositions.clear();
        endPositions.clear();
        for (int i = 0; i < dest.length(); ++i) {
            if (dest.charAt(i) == '?') {
                if (startPositions.size() == endPositions.size()) {
                    startPositions.add(i);
                } else {
                    endPositions.add(i);
                }
            }
        }

        boolean flag = false;
        for (int i = 0; i < startPositions.size(); ++i) {

            if (start > startPositions.get(i) && end <= endPositions.get(i)) {
                flag = true;
                break;
            }
        }

        if (flag == false) {

            if (str.equals("") && start < end) {
                return dest.subSequence(start, end);
            }

            return "";
        }
        return null;
    }
}
