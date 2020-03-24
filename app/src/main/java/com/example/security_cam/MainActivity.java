package com.example.security_cam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public final static String HOME_WIFI_AP_BSSID = "88:96:4e:a6:32:70";
    public final static String [] owner_devices = new String[]{"Raiy-iPhone", "Janice"};

    public static HashMap<String, Device> deviceList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView y = findViewById(R.id.textView);
        y.setText(CheckDevice.isNoAnyOwnersDeviceInHouse(this, HOME_WIFI_AP_BSSID));


    }

}