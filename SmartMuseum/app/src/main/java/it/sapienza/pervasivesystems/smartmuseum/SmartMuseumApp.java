package it.sapienza.pervasivesystems.smartmuseum;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by andrearanieri on 21/04/16.
 */
public class SmartMuseumApp extends Application {

//    private BeaconManager beaconManager;
//
//    private List<Region> regionsToMonitor;
//
//    @Override
//    public void onCreate() {
//        Log.i("ANDREA", "APPLICATION START**************************************");
//
//        super.onCreate();
//
//        beaconManager = new BeaconManager(getApplicationContext());
//
//        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
//            @Override
//            public void onEnteredRegion(Region region, List<Beacon> list) {
//                showNotification(
//                        "Welcome to Smart Museum!",
//                        "Enjoy your visit with our app. The more you know, the more you enjoy.");
//            }
//
//            @Override
//            public void onExitedRegion(Region region) {
//                showNotification(
//                        "Good Bye",
//                        "We hope to see you soon!");
//            }
//        });
//
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//            @Override
//            public void onServiceReady() {
//                for (Region r : SmartMuseumApp.initializeRegionsToMonitor())
//                    beaconManager.startMonitoring(r);
//            }
//        });
//    }
//
//    public void showNotification(String title, String message) {
//        Intent notifyIntent = new Intent(this, MainActivity.class);
//        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
//                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification notification = new Notification.Builder(this)
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .build();
//        notification.defaults |= Notification.DEFAULT_SOUND;
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, notification);
//    }
//
//    static private List<Region> initializeRegionsToMonitor() {
//        List<Region> ret = new ArrayList<Region>();
//        ret.add(new Region("Museum Entrance",
//                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 17957, 56571));
//        return ret;
//    }
}
