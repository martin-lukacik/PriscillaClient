package com.example.priscillaclient.misc;

import com.example.priscillaclient.R;

import java.util.HashMap;

public final class Preferences {
    // Preferences group
    public static final String PREFS = "settings";

    // Preferences
    public static final String PREFS_USERNAME = "username";
    public static final String PREFS_REFRESH_TOKEN = "refresh_token";
    public static final String PREFS_PINNED_COURSE_ID = "pinned_course_id";
    public static final String PREFS_THEME_ID = "theme_id";
    public static final String PREFS_LANGUAGE_SHORTCUT = "language_shortcut";
    public static final String PREFS_MOTIVE = "motive_index";

    public static final int[] PREFS_MOTIVES = new int[] {
        0,
        R.style.Purple,
        R.style.Blue,
        R.style.Green,
        R.style.Orange,
        R.style.Red,
        R.style.Colorblind,
    };
}
