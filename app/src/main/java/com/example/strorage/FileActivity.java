package com.example.strorage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.activetytest.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {

        super.onDestroy();
        EditText text = (EditText) findViewById(R.id.textView);
        save(text.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        save("hello");
        String data= Load();
        EditText text = (EditText) findViewById(R.id.textView);
        EditText text_sharde = (EditText) findViewById(R.id.edit_sharde);
        text.setText(data);
        text_sharde.setText(getSharedPreferences("data", MODE_PRIVATE).getString("text",""));
        Button shared_Btn = (Button) findViewById(R.id.but_sharde);
        shared_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences val = getSharedPreferences("data", MODE_PRIVATE);
                EditText text = (EditText) findViewById(R.id.edit_sharde);
                SharedPreferences.Editor edit = val.edit();
                edit.putString("text",text.getText().toString());
                edit.apply();

            }
        });
        Button SQLite_btn = (Button) findViewById(R.id.SQLite_btn);
        SQLite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FileActivity.this,SQLiteActivity.class));
            }
        });


    }




    @SuppressLint("SuspiciousIndentation")
    public  void save(String Text)  {
        FileOutputStream os=null;
        BufferedWriter  bw=null;
        try {
            os=openFileOutput("data", Context.MODE_PRIVATE);
            bw=new BufferedWriter(new OutputStreamWriter(os));
            bw.write(Text);
        } catch (Exception e) {
            throw new RuntimeException(e);
    }finally {
            try {
                if(bw!=null)
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String Load()  {
        FileInputStream in=null;
        BufferedReader reader=null;
        StringBuffer data=new StringBuffer();
        try {
            in= openFileInput("data");

            reader=new BufferedReader(new InputStreamReader(in));
            String Line="";
            while((Line=reader.readLine())!=null){data.append(Line);}
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
               if(reader!=null)
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return data.toString();
    }
}