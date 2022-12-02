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

    public ArrayList<Integer> startPositions = new ArrayList<>();
    public ArrayList<Integer> endPositions = new ArrayList<>();

    public void clear(Spanned dest) {
        startPositions.clear();
        endPositions.clear();
        for (int i = 0; i < dest.length(); ++i) {
            if (dest.charAt(i) == '|') {
                if (startPositions.size() == endPositions.size()) {
                    startPositions.add(i);
                } else {
                    endPositions.add(i);
                }
            }
        }
    }

    @Override
    public CharSequence filter(CharSequence str, int sstart, int send, Spanned dest, int start, int end) {

        clear(dest);

        for (int i = 0; i < startPositions.size(); ++i) {

            int startPos = startPositions.get(i);
            int endPos = endPositions.get(i);

            if (start > startPos && end <= endPos) {
                return null;
            }
        }

        if (str.equals("") && start < end) {
            return dest.subSequence(start, end);
        }

        return "";
    }
}
