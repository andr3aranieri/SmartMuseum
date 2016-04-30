package it.sapienza.pervasivesystems.smartmuseum;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;

import it.sapienza.pervasivesystems.smartmuseum.business.beacons.Monitoring;

/**
 * Created by andrearanieri on 21/04/16.
 */
public class SmartMuseumApp extends Application {

    private Monitoring beaconMonitoring = new Monitoring();
    static public boolean isUserInsideMuseum = false;

    @Override
    public void onCreate() {
        Log.i("SmartMuseumApp", "APPLICATION START**************************************");

        super.onCreate();

        //beacons monitoring to detect users entrance and exit;
        this.beaconMonitoring.app = this;
        this.beaconMonitoring.initMonitoring((ContextWrapper) getApplicationContext());
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}

