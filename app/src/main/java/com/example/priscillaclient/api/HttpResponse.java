package com.example.priscillaclient.api;

public interface HttpResponse<T> {
    void onUpdate(T response);
}
