package com.example.midea;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.activetytest.R;

public class MediaActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        Button note = (Button) findViewById(R.id.btn_note);
        note.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.btn_note){sendnote();}
    }

    private void sendnote() {
        NotificationManager noteManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel myChannel = new NotificationChannel("1", "myChannel", NotificationManager.IMPORTANCE_DEFAULT);
            noteManager.createNotificationChannel(myChannel);
        }
        Notification note = new NotificationCompat.Builder(this,"1")
                .setContentTitle("this is title")
                .setContentInfo("this is text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(PendingIntent.getActivity(this,0,new Intent(this, MediaActivity.class), PendingIntent.FLAG_IMMUTABLE))
                .setAutoCancel(true)
                //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)))
                //.setStyle(new NotificationCompat.BigTextStyle().bigText("ddmqddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"))
                .build();
        noteManager.notify(1,note);
    }

}