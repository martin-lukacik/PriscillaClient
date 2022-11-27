package com.example.priscillaclient.api;

public interface HttpResponse<T> {
    void onUpdate(T response); // TODO generify all calls or ungenerify interface
}
