package com.example.strorage;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.activetytest.R;
@SuppressLint("Range")
public class SQLiteActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        MySQLHelper mySQLHelper = new MySQLHelper(this, "user.db", null, 1);
        db=mySQLHelper.getWritableDatabase();
        Button add = (Button) findViewById(R.id.add_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues vals = new ContentValues();
                vals.put("id",3);
                vals.put("username","zhangsan");
                vals.put("password","123456");
                vals.put("age",11);
                //db.insert("user",null,vals);
                ContentValues vals1 = new ContentValues();
                vals1.put("id",5);
                vals1.put("username","lisi");
                vals1.put("password","123456");
                vals1.put("age",15);
                db.insert("user",null,vals1);

            }
        });
        Button vals=(Button) findViewById(R.id.select_btn);
        vals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = db.query("user", null, null, null, null, null, null);
                if(cursor.moveToFirst())
                {
                    do{
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("username"));
                        String password = cursor.getString(cursor.getColumnIndex("password"));
                        int age = cursor.getInt(cursor.getColumnIndex("age"));
                        Log.d("db",id+"  "+name+"  "+password+"  "+age);

                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });

    }
}