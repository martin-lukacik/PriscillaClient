package com.example.priscillaclient;

import android.content.Context;
import android.webkit.JavascriptInterface;

class TaskViewInterface {

    Context context;
    public String data;

    public TaskViewInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void sendData(String data) {
        this.data = data;
    }
}
