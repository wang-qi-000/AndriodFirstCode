package com.example.server;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.PixelCopy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownLoadTask extends AsyncTask<String,Integer,Integer> {
    private  DownLoadListener listener;
    private  static  final  int SUCCRDD=0;
    private  static  final  int FailED=1;
    private  static  final  int PAUSED=2;
    private  static  final  int CANCELED=3;
    private  int lastProgress=0;
    private  boolean isCanceled=false;
    private  boolean isPaused=false;

    public DownLoadTask(DownLoadListener listener) {
        this.listener = listener;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
       int progress=values[0];
       if(progress>lastProgress){
           listener.onProgress(progress);
           lastProgress=progress;
       }
    }

    @Override
    protected void onPostExecute(Integer status) {
        if(status==SUCCRDD){listener.onSuccess();}
        if(status==FailED){listener.onFail();}
        if(status==PAUSED){listener.onPaused();}
        if(status==CANCELED){listener.onCanceled();}
    }

    @Override
    protected Integer doInBackground(String... strings) {
        Log.d("down","doInBackground");
        InputStream is=null;
        RandomAccessFile save=null;
        File file=null;
        try{
            long downloadLength=0;
            String url=strings[0];
            String fileName=url.substring(url.lastIndexOf("/"));
            String directory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file=new File(directory+fileName);
            if(file.exists()){downloadLength= file.length();Log.d("down","downloadcontent  "+file.length());}
            long contntLength=getContentLength(url);
            if(contntLength==0){return FailED;}
            if(contntLength==downloadLength){Log.d("down","downloadcontent  "+downloadLength);return SUCCRDD;}
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().addHeader("RANGE", "bytes=" + downloadLength + "-")
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if(response==null){return FailED;}
            is=response.body().byteStream();
            save=new RandomAccessFile(file,"rw");
            save.seek(downloadLength);
            byte[] b=new byte[1024];
            int len;
            long total=downloadLength;
            while((len=is.read(b))!=-1){
                if(isCanceled){return  CANCELED;}
                if(isPaused){return PAUSED;}
                save.write(b,0,len);
                total+=(long) len;
                int progress=(int)(total*100/contntLength);
                publishProgress(progress);
            }
            response.body().close();
            return SUCCRDD;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(is!=null){is.close();}
                if(save!=null){save.close();}
                if(isCanceled&&file!=null){file.delete();}
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return FailED;
    }

    private long getContentLength(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response!=null&&response.isSuccessful()){
                long l = response.body().contentLength();
                response.close();
                Log.d("down","content  "+l);
                return l;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public void parse(){
        isPaused=true;
    }
    public void cancel(){
        isCanceled=true;
    }
}
