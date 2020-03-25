package com.example.security_cam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public final static String HOME_WIFI_AP_MAC = "88:96:4e:a6:32:70";
    public final static String [] owner_devices = new String[]{"Raiy-iPhone", "Janice"};
    public static boolean trigger = false;
    public static HashMap<String, CheckDevice> deviceList = new HashMap<>(); // Store device information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView y = findViewById(R.id.textView);
        autoRefresh(this, 2*60*1000, y);  // Scan devices in house periodically
        //y.setText(Boolean.toString(trigger));

    }

    private final Handler handler = new Handler();
    private void autoRefresh(final Context context, final int cycle_time, final TextView show){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String deviceScanResult = CheckDevice.isAnyOwnersDeviceInHouse(context, HOME_WIFI_AP_MAC);
                trigger=deviceScanResult.equals("No");  // If "No" owner's devices in house, trigger on
                show.setText(deviceScanResult);  // Showing results of device scanning (for debug purpose)
                autoRefresh(context, cycle_time, show);
            }
        }, cycle_time);
    }

}