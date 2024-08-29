package com.example.internet;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.activetytest.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InternetActivity extends AppCompatActivity implements  View.OnClickListener{
    TextView resText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        //webviewTest();
        Button netBtn = (Button) findViewById(R.id.btn_NetText);
         resText= (TextView) findViewById(R.id.text_res);
        netBtn.setOnClickListener(this);
        resText.setOnClickListener(this);
    }

    private void webviewTest() {
        WebView webview = (WebView) findViewById(R.id.web_view);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("https://www.baidu.com");
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.btn_NetText){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //sendHttpConnection();
                    sendOkhttp();
                }
            }).start();

        }

    }

    private void sendHttpConnection() {
        HttpURLConnection httpcnnt=null;
        BufferedReader read=null;
        try {
            httpcnnt=(HttpURLConnection) new URL("https://www.baidu.com").openConnection();
            httpcnnt.setConnectTimeout(8000);
            httpcnnt.setRequestMethod("GET");
            httpcnnt.setReadTimeout(8000);
            read=new BufferedReader(new InputStreamReader(httpcnnt.getInputStream()));
            StringBuffer res = new StringBuffer();
            String line;
            while((line=read.readLine())!=null){
                res.append(line);
            }
            show(res.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(read!=null){
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpcnnt!=null){httpcnnt.disconnect();}
        }

    }

    private void show(String res) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resText.setText(res);
            }
        });
    }

    private void sendOkhttp(){
        try {
            Log.d("test","here1");
            //String uri="http://172.24.28.162:88/data.xml";
            String uri="http://172.24.34.95:88/Jdata.json";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(uri).build();
            Response response = client.newCall(request).execute();
            //XmlwithPull(response.body().string());
            //XmlwithSAX(response.body().string());
            jsonwithJSON(response.body().string());
        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    private void jsonwithJSON(String s ) {
        Log.d("test","here");
        Gson gson = new Gson();
        List<app> res = gson.fromJson(s, new TypeToken<List<app>>() {
        }.getType());
        for(app var: res){
            Log.d("data Json",var.getId()+"  "+var.getName()+"  "+var.getVersion()+" ");
        }
    }

    private void XmlwithPull(String string) {
        try {

            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlPullParser.setInput(new StringReader(string));
            int eventType=xmlPullParser.getEventType();
            StringBuffer res=new StringBuffer();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                String nadename=xmlPullParser.getName();
                if(eventType==XmlPullParser.START_TAG){
                    if("id".equals(nadename)||"name".equals(nadename)||"version".equals(nadename)){
                        //Log.d("test",xmlPullParser.nextText());
                        res.append(xmlPullParser.nextText()+"   ");
                    }
                }
                if(eventType==XmlPullParser.END_TAG&&"app".equals(nadename)){
                    Log.d("text",res.toString());
                    res=new StringBuffer();
                }

                eventType=xmlPullParser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private  void XmlwithSAX(String string){
        try {
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            xmlReader.setContentHandler(new ContentHandler());
            xmlReader.parse(new InputSource(new StringReader(string)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}