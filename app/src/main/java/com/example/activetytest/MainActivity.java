package com.example.activetytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.LBStest.LBStestActivity;
import com.example.Material.MaterialActivity;
import com.example.contentProvider.ContentProviderActivity;
import com.example.internet.InternetActivity;
import com.example.midea.MediaActivity;
import com.example.server.ServerActivity;
import com.example.strorage.FileActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    //private  NetTest netTest;
        private IntentFilter intentFilter;
        private LocalBroadcastManager localBroadcastManager;
        private LocalReceiver localReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        intentFilter=new IntentFilter();
//        netTest= new NetTest();
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        registerReceiver(netTest,intentFilter);
        Button button=(Button) findViewById(R.id.button3);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(new LocalReceiver(),new IntentFilter("LocalBoardCast"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localBroadcastManager.sendBroadcast(new Intent("LocalBoardCast"));
//                Log.d("click","click B");
//                Intent intent=new Intent("myBoardCast");
//                intent.setPackage(getPackageName());
//                sendBroadcast(intent);
            }
        });

        btnBind(R.id.button4,FileActivity.class);
        btnBind(R.id.button5, ContentProviderActivity.class);
        btnBind(R.id.button6, MediaActivity.class);
        btnBind(R.id.button7, InternetActivity.class);
        btnBind(R.id.button8, ServerActivity.class);
        //btnBind(R.id.button9, MapActivity.class);
        btnBind(R.id.button9,LBStestActivity.class);
        btnBind(R.id.button10, MaterialActivity.class);
        //Log.d("SHA1",sHA1(this));
    }

    private void btnBind(int id,Class<?> ActivityClass) {
        Button button4 = (Button) findViewById(id);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                int i = 8;
//                int j = 8 / 0;
                startActivity(new Intent(MainActivity.this, ActivityClass));

            }
        });
    }

    class LocalReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LocalTest","Local received");
        Toast.makeText(context,"Local OK", Toast.LENGTH_LONG).show();
    }
}
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(netTest);
//    }
//
//    class NetTest extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            Log.d("net change","net changed");
//            ConnectivityManager CM=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo NetInfo = CM.getActiveNetworkInfo();
//
//            if(NetInfo!=null&&NetInfo.isAvailable()){
//                Toast.makeText(context,"net ok", Toast.LENGTH_LONG).show();
//                Log.d("net test","net OK");
//            }
//           else{
//                Toast.makeText(context,"net not ok", Toast.LENGTH_LONG).show();
//                Log.d("net test","net not OK");}
//            Toast.makeText(context,"net changes", Toast.LENGTH_LONG).show();
//        }
//    }
public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}