package com.example.priscillaclient.util;

import android.content.Context;

public class JavascriptInterface {

    Context context;
    public String data;

    public JavascriptInterface(Context context) {
        this.context = context;
    }

    @android.webkit.JavascriptInterface
    public void sendData(String data) {
        this.data = data;
    }
}
