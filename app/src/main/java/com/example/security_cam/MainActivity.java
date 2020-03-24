package com.example.security_cam;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static HashMap<String, Device> deviceList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("", "-----------------------");

        CheckDevice.getDeviceInLAN(deviceList);

        try {
            Thread.sleep(5000);
        } catch (Exception e){
            Log.v("pause",e.toString());
        }


        Log.v("main", deviceList.get("Raiy-NB").mac);
        TextView y = findViewById(R.id.textView);
        y.setText(deviceList.get("Raiy-iPhone").status);

    }



}