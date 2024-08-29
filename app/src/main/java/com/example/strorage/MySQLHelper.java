package com.example.strorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLHelper  extends SQLiteOpenHelper {
    public  static  final String   table_user="create table user" +
            "(id integer primary key autoincrement," +
            "username varchar(20)," +
            "password varchar(20)," +
            "age integer)";
    private Context mContext;
    public MySQLHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table_user);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
