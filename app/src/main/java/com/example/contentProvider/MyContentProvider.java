package com.example.contentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.strorage.MySQLHelper;

public class MyContentProvider extends ContentProvider {
    private  static MySQLHelper dbHelper;
    private static final int user_dir=0;
    private static final int user_item=1;
    private  static UriMatcher matcher;
    private static final  String auth = "com.example.contentProvider.MyContentProvider";
    public static final String TABLE = "user";

    static {
        matcher=new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(auth, TABLE,user_dir);
        matcher.addURI(auth,TABLE+"/#",user_item);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int res=-1;
        int code=matcher.match(uri);
        if(code==user_dir){
            res=db.delete(TABLE,selection,selectionArgs);
        }
        if(code==user_item){
            res=db.delete(TABLE,"id=?",new String[]{uri.getPathSegments().get(1)});
        }
        return res;
    }

    @Override
    public String getType(Uri uri) {
        String res="";
        if(matcher.match(uri)==user_dir){res= "vnd.android.cursor.dir/vnd."+auth+".user";};
        if(matcher.match(uri)==user_item){res= "vnd.android.cursor.item/vnd."+auth+".user";};
        return  res;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri res=null;
        int code=matcher.match(uri);
        if(code==user_dir){
            long id=db.insert(TABLE,null,values);
            res=Uri.parse("content://" + auth + "/"+TABLE+"/" + id);
        }
        return  res;
    }

    @Override
    public boolean onCreate() {
        dbHelper=new MySQLHelper(this.getContext(),"user.db",null,1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor=null;
        int code=matcher.match(uri);
        if(code==user_dir){

            cursor=db.query(TABLE,projection,selection,selectionArgs,null,null,sortOrder);
        }
        if(code==user_item){
            String id=uri.getPathSegments().get(1);
            cursor=db.query(TABLE,projection,"id=?",new String[]{id},null,null,sortOrder);
        }
        // TODO: Implement this to handle query requests from clients.
        return  cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int res=-1;
        if(matcher.match(uri)==user_item){
            res=db.update(TABLE,values,"id=?",new String[]{uri.getPathSegments().get(1)});

        }
        return  res;
    }
}