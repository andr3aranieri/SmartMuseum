package it.sapienza.pervasivesystems.smartmuseum.business.exhibits;

import com.estimote.sdk.Beacon;

import java.util.ArrayList;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.BeaconBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.db.ExhibitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;

/**
 * Created by andrearanieri on 01/05/16.
 */
public class ExhibitBusiness {

    private ExhibitDB exhibitDB = new ExhibitDB();
    private BeaconBusiness beaconBusiness = new BeaconBusiness();

    public ArrayList<ExhibitModel> getSortedExhibits(List<Beacon> sortedBeacons) {
        ArrayList<ExhibitModel> sortedExhibits = null;
        if(SmartMuseumApp.unsortedExhibits != null) {
            sortedExhibits = new ArrayList<ExhibitModel>();
            ExhibitModel em = null;
            String bKey;
            for(Beacon b: sortedBeacons) {
                bKey = this.beaconBusiness.getBeaconHashmapKey(b);
                if(SmartMuseumApp.unsortedExhibits.containsKey(bKey)) {
                    em = SmartMuseumApp.unsortedExhibits.get(bKey);
                    sortedExhibits.add(em);
                }
            }
        }
        else {
            sortedExhibits = null;
        }
        return sortedExhibits;
    }

    public ExhibitModel getExhibitDetail(String key) {
        return SmartMuseumApp.unsortedExhibits.get(key);
    }

    public boolean hasOrderingChanged(ArrayList<ExhibitModel> oldList, ArrayList<ExhibitModel> newList) {
        boolean hasChanged = true;

        return hasChanged;
    }

    //key used to retrieve exhibits from the application level hashmap that contains all of them;
    public String getExhibitHashmapKey(ExhibitModel em) {
        return em.getBeaconMajor().concat(":").concat(em.getBeaconMinor());
    }
}
