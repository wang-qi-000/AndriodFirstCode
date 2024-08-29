package com.example.activetytest;

import android.content.Context;
import android.widget.Toast;

public class MyShowUtil {
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MyShowUtil(Context context) {
        this.context = context;
    }

    private Context context;

    public   void  showToast(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


}
