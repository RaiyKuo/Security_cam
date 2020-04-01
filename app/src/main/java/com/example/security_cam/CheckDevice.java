package com.example.security_cam;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.HashMap;


public class CheckDevice{

    String mac, status, last_activity;
    public CheckDevice(String mac, String status, String last_activity){
        this.mac = mac;
        this.status = status;
        this.last_activity = last_activity;
    }

    public static void getDeviceInLAN(){
        Thread thread = new Thread(new Runnable() {    // Execute by a independent thread
            @Override
            public void run() {
                try {
                    Log.v("", "test");
                    String url = "http://192.168.1.254/cgi-bin/devices.ha";
                    Elements table = Jsoup.connect(url).get().select("tr");
        // Download HTML form the "Device List" page provided by the gateway machine of home wifi AP

                    Element row;
                    String property;
                    HashMap<String, String> items = new HashMap<>();

                    for(int i = table.size()-1; i>=0; i--){
                        row = table.get(i);                          // Parse each row in the table
                        property = row.select("th").html();
                        items.put(property, row.select("td").html());

                        if (property.equals("MAC Address")) {
                            MainActivity.deviceList.put(items.get("IPv4 Address / Name").split(" / ",2)[1],
                                    new CheckDevice(items.get("MAC Address"),
                                            items.get("Status"),
                                            items.get("Last Activity")));
                        }
                    }  // Load device info into the deviceList
                }catch(Exception e){
                    Log.v("Jsoup error", e.toString());
                }
            }
        });
        thread.start();
    }

    public static String isAnyOwnersDeviceInHouse(Context context, String home_wifi){

        WifiManager mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifi = mainWifi.getConnectionInfo();

        if (wifi.getBSSID().equals(home_wifi)){ // If the current internet connection is Home Wi-Fi
            CheckDevice.getDeviceInLAN();  // Update device list

            try {
                Thread.sleep(1000);     // Wait 5 sec for getDeviceInLAN() to finish updating
            } catch (Exception e){Log.v("pause",e.toString());}

            for(String device:MainActivity.owner_devices){
                if(MainActivity.deviceList.containsKey(device)) {
                    if(MainActivity.deviceList.get(device).status.equals("on"))
                        return device;
                }  // Check if there's any owner's device in house; if yes, return one of them
            }
            return "None";   // Connected to designated "Home WiFi", but no any owner's devices
        }
        return "Not connected to HOME WIFI";
    }
}
