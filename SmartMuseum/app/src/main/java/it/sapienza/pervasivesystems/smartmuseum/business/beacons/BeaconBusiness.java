package it.sapienza.pervasivesystems.smartmuseum.business.beacons;

import com.estimote.sdk.Beacon;

/**
 * Created by andrearanieri on 01/05/16.
 */
public class BeaconBusiness {

    public String getBeaconHashmapKey(Beacon b) {
        return (""+b.getMajor()).concat(":").concat(""+b.getMinor());
    }
}
