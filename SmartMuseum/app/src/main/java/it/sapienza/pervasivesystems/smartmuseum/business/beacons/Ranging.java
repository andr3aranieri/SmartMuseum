package it.sapienza.pervasivesystems.smartmuseum.business.beacons;

import android.app.Activity;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;

/**
 * Created by andrearanieri on 29/04/16.
 */
public class Ranging {

    private BeaconManager beaconManager;
    private Activity activity;
    public static RangingDetection rangingDetection;

    public Ranging(Activity a) {
        this.activity = a;
    }

    public void initRanging() {
        this.beaconManager = new BeaconManager(activity);

        this.beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    ILCMessage message = new ILCMessage();
                    message.setMessageType(ILCMessage.MessageType.INFO);
                    message.setMessageText("Beacons detected");
                    message.setMessageObject(list);
                    rangingDetection.beaconsDetected(message);
                }
            }
        });
    }

    public void startRanging() {
        this.beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                for(Region r: initializeRegionsToRange())
                    beaconManager.startRanging(r);
            }
        });
    }

    public void stopRanging() {
        for(Region r: initializeRegionsToRange()) {
            Log.i("STOP RANGING", r.getProximityUUID().toString());
            beaconManager.stopRanging(r);
        }
    }

    static private List<Region> initializeRegionsToRange() {
        //TODO: read beacons to monitor from DB;

        List<Region> ret = new ArrayList<Region>();
        ret.add(new Region("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null));
        return ret;
    }

}