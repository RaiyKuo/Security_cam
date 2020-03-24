package com.example.security_cam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public final static String HOME_WIFI_AP_BSSID = "88:96:4e:a6:32:70";

    public static HashMap<String, Device> deviceList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("", "-----------------------");
        TextView y = findViewById(R.id.textView);

        if (CheckDevice.getMACofCurrentWiFi(this).equals(HOME_WIFI_AP_BSSID))
            y.setText("Yes");




        /*
        CheckDevice.getDeviceInLAN(deviceList);

        try {
            Thread.sleep(5000);
        } catch (Exception e){
            Log.v("pause",e.toString());
        }
        y.setText("Raiy-iPhone "+ deviceList.get("Raiy-NB").mac +" "+deviceList.get("Raiy-iPhone").status);
        */

    }

}