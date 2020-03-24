package com.example.security_cam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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

        String BSSID, SSID, MAC;
        WifiManager mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifi = mainWifi.getConnectionInfo();
        SSID = wifi.getSSID();      // SSID:   Name of Wi-Fi Access Point
        BSSID = wifi.getBSSID();    // BSSID:  MAC address of Wi-Fi Access Point


        /*
        CheckDevice.getDeviceInLAN(deviceList);

        try {
            Thread.sleep(5000);
        } catch (Exception e){
            Log.v("pause",e.toString());
        }
        */


        TextView y = findViewById(R.id.textView);
        //y.setText("Raiy-iPhone "+ deviceList.get("Raiy-NB").mac +" "+deviceList.get("Raiy-iPhone").status);
        y.setText(SSID+ " "+ BSSID );

    }



}