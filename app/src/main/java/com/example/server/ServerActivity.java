package com.example.server;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.activetytest.R;

public class ServerActivity extends AppCompatActivity implements View.OnClickListener{
private  MyService.DownLoadBinder ibind;
private ServiceConnection mycon=new ServiceConnection(){

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ibind=(MyService.DownLoadBinder) iBinder;
        ibind.stratDownLoad();
        ibind.getProgress();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
};
private  DownloadService.downloadBind bind;
private ServiceConnection conn=new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        bind=(DownloadService.downloadBind) iBinder;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        Button start = (Button) findViewById(R.id.server_strat);
        Button stop = (Button) findViewById(R.id.server_stop);
       start.setOnClickListener(this);
       stop.setOnClickListener(this);
        Button D_start = (Button) findViewById(R.id.download_start);
        Button D_purse = (Button) findViewById(R.id.download_purse);
        Button D_cancel = (Button) findViewById(R.id.download_cancel);
        D_cancel.setOnClickListener(this);
        D_purse.setOnClickListener(this);
        D_start.setOnClickListener(this);
        Intent intent=new Intent(this,DownloadService.class);
        startService(intent);
        bindService(intent,conn,BIND_AUTO_CREATE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            }else{
                Log.d("Permission","you denied the permission");
            }
        }

    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MyService.class);
        if(view.getId()==R.id.server_strat){
            //startService(intent);
            bindService(intent,mycon,BIND_AUTO_CREATE);
        }if(view.getId()==R.id.server_stop){
            //stopService(intent);
            unbindService(mycon);
        }
        if(view.getId()==R.id.download_start){
            bind.startDownload("https://dldir1v6.qq.com/weixin/Windows/WeChatSetup.exe");
        }
        if(view.getId()==R.id.download_cancel){
            bind.cancel();
        }
        if(view.getId()==R.id.download_purse){
            bind.pause();
        }
    }
}