package com.example.priscillaclient.api;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ApiTask {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private String error = null;

    public interface Callback<T> {
        void onComplete(T result, String error);
    }

    public <T> void executeAsync(Callable<T> callable, Callback<T> callback) {
        executor.execute(() -> {
            T result = null;
            try {
                result = callable.call();
            } catch (Exception e) {
                setError(e.getMessage());
            }
            final T r = result;
            handler.post(() -> {
                callback.onComplete(r, getError());
            });
        });
    }

    private void setError(String message) {
        error = message;
    }

    public String getError() {
        String e = error;
        error = null;
        return e;
    }
}
