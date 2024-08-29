package com.example.LBStest;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapsdkplatform.comapi.BMapManagerInternal;
import com.example.activetytest.R;

import java.util.ArrayList;
import java.util.List;

public class LBStestActivity extends AppCompatActivity {
private TextView location_text;
private LocationClient locationClient;
private MapView mapView;



    @Override
    protected void onDestroy() {
        super.onDestroy();
       // mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
      // mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
       //mapView.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationClient.setAgreePrivacy(true);
        SDKInitializer.setAgreePrivacy(getApplicationContext(),true);
        SDKInitializer.initialize(getApplicationContext());
        try {
            locationClient = new LocationClient(getApplicationContext());
            locationClient.registerLocationListener(new BDLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation location) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("LBShere","onReceiveLocation");
                            location_text.setText("经度"+location.getLatitude()+"   维度"+location.getLongitude());
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //mapView=new MapView(this);
        setContentView(R.layout.activity_lbstest);

        Button btn_LBS = (Button) findViewById(R.id.btn_LBS);
        btn_LBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LBShere","3");
                locationClient.start();
            }
        });
        location_text=findViewById(R.id.text_LBS);

        List<String> permission =new ArrayList<>();
        permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permission.add(Manifest.permission.READ_PHONE_STATE);
        permission.add((Manifest.permission.ACCESS_COARSE_LOCATION));
        //permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(LBStestActivity.this,permission.toArray(new String[3]),1);

    }

}