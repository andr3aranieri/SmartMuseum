package it.sapienza.pervasivesystems.smartmuseum.business.beacons;

import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;

/**
 * Created by andrearanieri on 29/04/16.
 */
public interface RangingDetection {
        public void beaconsDetected(ILCMessage message);
}
