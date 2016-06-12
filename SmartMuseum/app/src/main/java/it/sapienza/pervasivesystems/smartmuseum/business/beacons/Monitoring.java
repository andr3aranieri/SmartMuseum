package it.sapienza.pervasivesystems.smartmuseum.business.beacons;

import android.content.ContextWrapper;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.Date;

import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;

/**
 * Created by andrearanieri on 29/04/16.
 */
public class Monitoring {

    private BeaconManager beaconManager;
    public static SmartMuseumApp app;

    public void initMonitoring(ContextWrapper context) {
        beaconManager = new BeaconManager(context);

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.i("Monitoring", "OnEnteredRegion. DetectedExhibits: " + app.detectedSortedExhibits + ", lastSeenBeaconTimeStamp: " + app.lastSeenBeaconTimeStamp);
                if(app.lastSeenBeaconTimeStamp == null && (app.detectedSortedExhibits == null || app.detectedSortedExhibits.size() == 0)) {
                    app.showNotification(
                            "Welcome to Smart Museum!",
                            "Enjoy your visit with our app. The more you know, the more you enjoy.");
                }
            }

            @Override
            public void onExitedRegion(Region region) {
                Log.i("Monitoring", "OnExitRegion. DetectedExhibits: " + app.detectedSortedExhibits + ", lastSeenBeaconTimeStamp: " + app.lastSeenBeaconTimeStamp);
                if((app.detectedSortedExhibits == null || app.detectedSortedExhibits.size() == 0) && (app.lastSeenBeaconTimeStamp != null && app.lastSeenBeaconTimeStamp.getTime() - new Date().getTime() > 60000)) {
                    app.showNotification(
                            "Good Bye",
                            "We hope to see you soon!");
                }
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                for (Region r : initializeRegionsToMonitor())
                    beaconManager.startMonitoring(r);
            }
        });
    }

    static private List<Region> initializeRegionsToMonitor() {
        //TODO: read beacons to monitor from DB;

        List<Region> ret = new ArrayList<Region>();
        ret.add(new Region("Museum Entrance",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 17957, 56571));
        return ret;
    }
}
