package it.sapienza.pervasivesystems.smartmuseum.business.exhibits;

import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;

/**
 * Created by andrearanieri on 07/05/16.
 */
public class WorkofartBusiness {

    public String getWorkofartHashmapKey(ExhibitModel em, WorkofartModel wam) {
        return em.getBeaconMajor().concat(":").concat(em.getBeaconMinor()).concat(new Integer(wam.getId()).toString());
    }
}
