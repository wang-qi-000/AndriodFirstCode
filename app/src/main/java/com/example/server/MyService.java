package com.example.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private DownLoadBinder bind= new DownLoadBinder();
    class DownLoadBinder extends Binder{
        public void stratDownLoad(){
            Log.d("MyService","stratDownLoad");
        }
        public int getProgress(){
            Log.d("MyService","getProgress");
            return 0;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService","onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d("MyService","onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  bind;
        // TODO: Return the communication channel to the service.

    }
}