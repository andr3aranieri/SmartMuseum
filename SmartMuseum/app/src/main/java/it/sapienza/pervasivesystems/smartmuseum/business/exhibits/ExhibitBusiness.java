package it.sapienza.pervasivesystems.smartmuseum.business.exhibits;

import android.util.Log;

import com.estimote.sdk.Beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.BeaconBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.db.ExhibitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.db.VisitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.db.WorkofartDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitExhibitModel;

/**
 * Created by andrearanieri on 01/05/16.
 */
public class ExhibitBusiness {

    private ExhibitDB exhibitDB = new ExhibitDB();
    private WorkofartDB workofartDB = new WorkofartDB();
    private BeaconBusiness beaconBusiness = new BeaconBusiness();
    private VisitDB visitDB = new VisitDB();

    public HashMap<String, ExhibitModel> getUnorderedExhibits() {
        return this.exhibitDB.getExhibitsFromDB2();
    }

    public ArrayList<ExhibitModel> getSortedExhibits(List<Beacon> sortedBeacons) {
        ArrayList<ExhibitModel> sortedExhibits = null;
        if(SmartMuseumApp.unsortedExhibits != null) {
            sortedExhibits = new ArrayList<ExhibitModel>();
            ExhibitModel em = null;
            VisitExhibitModel visitExhibitModel = null;
            String bKey;
            Log.i("BeaconsDetected", "*******************************");
            for(Beacon b: sortedBeacons) {
                bKey = this.beaconBusiness.getBeaconHashmapKey(b);
                if(SmartMuseumApp.unsortedExhibits.containsKey(bKey)) {
                    Log.i("BeaconsDetected", "key: " + bKey + " YES");
                    em = SmartMuseumApp.unsortedExhibits.get(bKey);

                    //timestamp and color;
                    if(SmartMuseumApp.todayVisitedExhibits != null && SmartMuseumApp.todayVisitedExhibits.containsKey(bKey)) {
                        visitExhibitModel = SmartMuseumApp.todayVisitedExhibits.get(bKey);
                        em.setTimestamp(visitExhibitModel.getTimeStamp());
                        em.setColor("#00ffbf");
                    }
                    else {
                        em.setColor("#ffe6e6");
                    }

                    sortedExhibits.add(em);
                }
                else {
                    Log.i("BeaconsDetected", "key: " + bKey + " NO");
                }
            }
            Log.i("BeaconsDetected", "*******************************");
        }
        else {
            sortedExhibits = null;
        }
        return sortedExhibits;
    }

    public ExhibitModel getExhibitDetail(String key) {
        return SmartMuseumApp.unsortedExhibits.get(key);
    }

    public ArrayList<ExhibitModel> getUnorderedExhibitList(HashMap<String, ExhibitModel> map) {
        return new ArrayList<ExhibitModel>(map.values());
    }

    public ArrayList<ExhibitModel> getUserExhibitHistory(UserModel um) {
        return this.exhibitDB.getUserExhibitHistory(um);
    }

    public HashMap<String, VisitExhibitModel> getTodayUserExhibitVisitsHistoryMap(UserModel userModel) {
        return this.visitDB.getTodayUserExhibitHistoryHashMap(userModel);
    }

    public HashMap<String, VisitExhibitModel> getUserExhibitVisitsHistoryMap(UserModel userModel) {
        return this.visitDB.getUserExhibitHistoryHashMap(userModel);
    }

    public List<VisitExhibitModel> getUserExhibitHistoryList(UserModel userModel) {
        return this.visitDB.getUserExhibitHistoryList(userModel);
    }

    public boolean hasAlreadyVisited(String key, HashMap<String, VisitExhibitModel> exhibitsHistoryMap) {
        boolean ret = false;
        VisitExhibitModel vem = null;
        if(exhibitsHistoryMap.containsKey(key)) {
            ret = true;
        }
        else {
            ret = false;
        }
        return ret;
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
        int index = 0;

        //if the number of detected beacons changes, newList will be surely different from oldList;
        if(oldList == null || oldList.size() == 0 || newList == null || newList.size() == 0 || (oldList.size() != newList.size()))
            return true;

        //if the number of elements in the 2 lists is the same, we check their ordering;
        for(ExhibitModel em: oldList) {
            index = oldList.indexOf(em);
            if(this.getExhibitHashmapKey(newList.get(index)).compareTo(this.getExhibitHashmapKey(em)) != 0) {
                return true;
            }
        }
        return false;
    }

    //key used to retrieve exhibits from the application level hashmap that contains all of them;
    public String getExhibitHashmapKey(ExhibitModel em) {
        return this.exhibitDB.getExhibitHashmapKey(em);
    }
}
