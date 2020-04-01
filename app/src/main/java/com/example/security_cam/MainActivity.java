package com.example.security_cam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import java.util.HashMap;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import net.ossrs.rtmp.ConnectCheckerRtmp;


public class MainActivity extends AppCompatActivity
        implements ConnectCheckerRtmp, SurfaceHolder.Callback{

    public final static String HOME_WIFI_AP_MAC = "88:96:4e:a6:32:70";
    public final static String rtmp_stream_URL = "rtmp://192.168.1.84:1935/live/android";

    public final static String [] owner_devices = new String[]{"Raiy-iPhone", "Janice"};
    public static boolean trigger = false;
    public static HashMap<String, CheckDevice> deviceList = new HashMap<>(); // Store device information

    private RtmpCamera1 rtmpCamera1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        rtmpCamera1 = new RtmpCamera1(surfaceView, this);
        rtmpCamera1.setReTries(10);
        surfaceView.getHolder().addCallback(this);

        autoRefresh(this, 15*1000, (TextView)findViewById(R.id.scan_results));  //Scan devices periodically
    }

    private final Handler handler = new Handler();
    private void autoRefresh(final Context context, final int cycle_time, final TextView show){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String deviceScanResult = CheckDevice.isAnyOwnersDeviceInHouse(context, HOME_WIFI_AP_MAC);

                trigger=deviceScanResult.equals("No");  // If "No" owner's devices in house, trigger on
                show.setText(deviceScanResult);  // Showing results of device scanning (for debug purpose)

                if(trigger){
                    startSurveillance();
                }
                else{
                    stopSurveillance();
                }

                autoRefresh(context, cycle_time, show);
            }
        }, cycle_time);
    }

    public void startSurveillance(){
        if (!rtmpCamera1.isStreaming()) {
            if (rtmpCamera1.isRecording()
                    || rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                rtmpCamera1.startStream(rtmp_stream_URL);
            } else {
                Toast.makeText(this, "Error preparing stream, This device cant do it",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void stopSurveillance(){
        rtmpCamera1.stopStream();
    }

    @Override
    public void onConnectionSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Connection success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rtmpCamera1.reTry(5000, reason)) {
                    Toast.makeText(MainActivity.this, "Retry", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(MainActivity.this, "Connection failed. " + reason, Toast.LENGTH_SHORT)
                            .show();
                    rtmpCamera1.stopStream();
                }
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        rtmpCamera1.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (rtmpCamera1.isStreaming()) {
            rtmpCamera1.stopStream();
        }
        rtmpCamera1.stopPreview();
    }

    @Override
    public void onDisconnectRtmp(){}

    @Override
    public void onAuthErrorRtmp(){}

    @Override
    public void onAuthSuccessRtmp(){}

    @Override
    public void onNewBitrateRtmp(long bitrate) {}

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {}
}