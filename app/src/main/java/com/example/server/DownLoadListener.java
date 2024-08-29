package com.example.server;

public interface DownLoadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFail();
    void onPaused();
    void onCanceled();
}
