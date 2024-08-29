package com.example.server;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.activetytest.R;
import com.example.midea.MediaActivity;

import java.io.File;

public class DownloadService extends Service {
    private DownLoadTask downLoadTask;
    private String Url;
    private boolean first=true;
    private DownLoadListener listener=new DownLoadListener() {
        @Override
        public void onProgress(int progress) {
            show("Downloading....",progress);
        }

        @Override
        public void onSuccess() {
           downLoadTask=null;
           stopForeground(true);
           show("Download Succedd",-1);
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFail() {
            downLoadTask=null;
            stopForeground(true);
            show("Download Failed",-1);
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
           downLoadTask=null;
            Toast.makeText(DownloadService.this, "Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
          downLoadTask=null;
          Log.d("down","canceled");
          stopForeground(true);
            Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    };
    private downloadBind bind=new downloadBind();
    class  downloadBind extends Binder {
        public void startDownload(String url){
           if(downLoadTask!=null){return;}
            Log.d("down","startDownload");
               Url=url;
               downLoadTask=new DownLoadTask(listener);
               downLoadTask.execute(url);
               show("Downloading...",0);
            Toast.makeText(DownloadService.this, "Downloading.....", Toast.LENGTH_SHORT).show();
        }
        public void pause(){
            if(downLoadTask==null){return ;}
            downLoadTask.parse();
        }
        public void cancel(){
            Log.d("down","bind cancel");
            if(downLoadTask!=null){downLoadTask.cancel();return;}
            if(Url==null){return ;}
            String fileName=Url.substring(Url.lastIndexOf("/"));
            String directory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            File file=new File(directory+fileName);
            if(file.exists()){file.delete();}
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bind;
    }
    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    private void show(String title ,int progress){
        NotificationManager noteManager = getNotificationManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel myChannel = new NotificationChannel("1", "myChannel", NotificationManager.IMPORTANCE_DEFAULT);
            noteManager.createNotificationChannel(myChannel);
        }
        NotificationCompat.Builder note = new NotificationCompat.Builder(this,"1")
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(PendingIntent.getActivity(this,0,new Intent(this, ServerActivity.class), PendingIntent.FLAG_IMMUTABLE));
                if(progress>=0) {
                       note.setContentText(progress + "%")
                       .setProgress(100, progress, false);
                }
                if(first){startForeground(1,note.build());first=false;return;}
                noteManager.notify(1,note.build());
    }
}
