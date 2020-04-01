package com.example.security_cam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.view.SurfaceView;

import com.pedro.rtplibrary.rtmp.RtmpCamera1;

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

        TextView surveillance_on = findViewById(R.id.surveillance_on);

        //autoRefresh(this, 2*60*1000, (TextView)findViewById(R.id.scan_results));
        // Scan devices periodically
        //surveillance_on.setText(Boolean.toString(trigger));

        /*
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        ConnectCheckerRtmp connectCheckerRtmp;
        //create builder
        RtmpCamera1 rtmpCamera1 = new RtmpCamera1(surfaceView, connectCheckerRtmp);
        //start stream
        if (rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
            rtmpCamera1.startStream("rtmp://192.168.1.84:1935/live/");
        } else {
            //This device cant init encoders, this could be for 2 reasons: The encoder selected doesnt support any configuration setted or your device hasnt a H264 or AAC encoder (in this case you can see log error valid encoder not found)
        }
        //stop stream
        rtmpCamera1.stopStream();
        */

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