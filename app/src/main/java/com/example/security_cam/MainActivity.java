package com.example.security_cam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import java.util.HashMap;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import net.ossrs.rtmp.ConnectCheckerRtmp;


public class MainActivity extends AppCompatActivity
        implements ConnectCheckerRtmp, View.OnClickListener, SurfaceHolder.Callback{

    public final static String HOME_WIFI_AP_MAC = "88:96:4e:a6:32:70";
    public final static String rtmp_stream_URL = "rtmp://192.168.1.84:1935/live/android";

    public final static String [] owner_devices = new String[]{"Raiy-iPhone", "Janice"};
    public static boolean trigger = false;
    public static HashMap<String, CheckDevice> deviceList = new HashMap<>(); // Store device information

    private RtmpCamera1 rtmpCamera1;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        button = findViewById(R.id.b_start_stop);
        button.setOnClickListener(this);
        rtmpCamera1 = new RtmpCamera1(surfaceView, this);
        rtmpCamera1.setReTries(10);
        surfaceView.getHolder().addCallback(this);

        /*
        TextView surveillance_on = findViewById(R.id.surveillance_on);
        autoRefresh(this, 2*60*1000, (TextView)findViewById(R.id.scan_results));
         Scan devices periodically
        surveillance_on.setText(Boolean.toString(trigger));
        */
    }

    private final Handler handler = new Handler();
    private void autoRefresh(final Context context, final int cycle_time, final TextView show){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String deviceScanResult = CheckDevice.isAnyOwnersDeviceInHouse(context, HOME_WIFI_AP_MAC);
                trigger=deviceScanResult.equals("No");  // If "No" owner's devices in house, trigger on
                //show.setText(deviceScanResult);  // Showing results of device scanning (for debug purpose)
                autoRefresh(context, cycle_time, show);
            }
        }, cycle_time);
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
    public void onDisconnectRtmp() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(StreamRTMP.this, "Disconnected", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onAuthErrorRtmp() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainActivity.this, "Auth error", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onAuthSuccessRtmp() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainActivity.this, "Auth success", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        if (!rtmpCamera1.isStreaming()) {
            if (rtmpCamera1.isRecording()
                    || rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                rtmpCamera1.startStream(rtmp_stream_URL);
            } else {
                Toast.makeText(this, "Error preparing stream, This device cant do it",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            rtmpCamera1.stopStream();
        }
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
    public void onNewBitrateRtmp(long bitrate) {}

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {}
}