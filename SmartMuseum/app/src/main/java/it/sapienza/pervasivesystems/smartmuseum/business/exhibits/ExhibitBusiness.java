package it.sapienza.pervasivesystems.smartmuseum.business.exhibits;

import com.estimote.sdk.Beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.BeaconBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.db.ExhibitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.db.VisitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitExhibitModel;

/**
 * Created by andrearanieri on 01/05/16.
 */
public class ExhibitBusiness {

    private ExhibitDB exhibitDB = new ExhibitDB();
    private BeaconBusiness beaconBusiness = new BeaconBusiness();
    private VisitDB visitDB = new VisitDB();

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

    public HashMap<String, VisitExhibitModel> getUserExhibitsHistoryMap(UserModel userModel) {
        return this.visitDB.getUserExhibitHistory(userModel);
    }

    public ArrayList<VisitExhibitModel> getUserExhibitHistoryList(HashMap<String, VisitExhibitModel> userHistoryMap) {
        return (ArrayList<VisitExhibitModel>) userHistoryMap.values();
    }

    public VisitExhibitModel getVisitExhibitDetail(HashMap<String, VisitExhibitModel> userHistoryMap, String key) {
        VisitExhibitModel visitExhibitModel;
        if (userHistoryMap.containsKey(key)) {
            visitExhibitModel = userHistoryMap.get(key);
        }
        else {
            visitExhibitModel = null;
        }

        return visitExhibitModel;
    }

    public boolean hasOrderingChanged(ArrayList<ExhibitModel> oldList, ArrayList<ExhibitModel> newList) {
        boolean hasChanged = true;

        return hasChanged;
    }

    //key used to retrieve exhibits from the application level hashmap that contains all of them;
    public String getExhibitHashmapKey(ExhibitModel em) {
        return this.exhibitDB.getExhibitHashmapKey(em);
    }
}
