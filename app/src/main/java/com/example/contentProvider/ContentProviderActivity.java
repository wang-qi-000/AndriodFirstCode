package com.example.contentProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.activetytest.MainActivity;
import com.example.activetytest.R;

import java.util.ArrayList;
import java.util.List;

public class ContentProviderActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    List<String> contactList=new ArrayList<>();
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                call();
            }else{
                Log.d("Permission","you denied the permission");
            }
        }
        if(requestCode==2){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                initContact();
            }else{
                Log.d("Contact permission","you denied the permission");
            }
        }
    }

    @SuppressLint({"Range", "NewApi"})
    private void initContact() {
        Cursor cursor=null;
        cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null);
        if(cursor==null){cursor.close();return;}
        try {
            while (cursor.moveToNext()){
                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactList.add(name+"    "+number);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            cursor.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);
        Button call_btn = (Button) findViewById(R.id.call_btn);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ContentProviderActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ContentProviderActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                }else {
                call();}
            }
        });
        ListView con_view = (ListView) findViewById(R.id.contact_view);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contactList);
        con_view.setAdapter(adapter);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},2);
        }else{
            initContact();
        }
        ContentResolver resolver = getContentResolver();
        String type=resolver.getType(Uri.parse("content://com.example.contentProvider.MyContentProvider/user/1"));
        Log.d("provider test",type);
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}