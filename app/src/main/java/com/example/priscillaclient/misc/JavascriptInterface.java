package com.example.priscillaclient.misc;

public class JavascriptInterface {

    public String data;

    public JavascriptInterface() { }

    @android.webkit.JavascriptInterface
    public void sendData(String data) {
        this.data = data;
    }
}
