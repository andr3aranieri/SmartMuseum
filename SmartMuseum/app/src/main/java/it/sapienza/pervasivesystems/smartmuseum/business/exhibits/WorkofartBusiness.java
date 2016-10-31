package it.sapienza.pervasivesystems.smartmuseum.business.exhibits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.model.db.VisitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.db.WorkofartDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitWorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;

/**
 * Created by andrearanieri on 07/05/16.
 */
public class WorkofartBusiness {

    private WorkofartDB workofartDB = new WorkofartDB();
    private VisitDB visitDB = new VisitDB();

    public HashMap<String, VisitWorkofartModel> getTodayUserWorksofartHistoryHashMap(UserModel userModel) {
        return this.visitDB.getTodayUserWorkofartHistoryHashMap(userModel);
    }

    public HashMap<String, VisitWorkofartModel> getTotalUserWorksofartHistoryHashMap(UserModel userModel) {
        return this.visitDB.getTotalUserWorkofartHistoryHashMap(userModel);
    }

    public List<VisitWorkofartModel> getUserWorkofartHistoryList(UserModel um) {
        return this.workofartDB.getUserWorkofartHistoryList(um);
    }

    public HashMap<String, WorkofartModel> getWorkofarts(ExhibitModel exhibitModel) {
        return this.workofartDB.getWorkofarts(exhibitModel);
    }

    public ArrayList<WorkofartModel> getWorksofartList(HashMap<String, WorkofartModel> map) {
        return new ArrayList<WorkofartModel>(map.values());
    }

    public String getWorkofartHashmapKey(String exhibitKey, WorkofartModel wam) {
        return this.workofartDB.getWorkofartHashmapKey(exhibitKey, wam);
    }

    public WorkofartModel getWorkofartDetail(HashMap<String, WorkofartModel> map, String key) {
        WorkofartModel workofartModel;
        if (map.containsKey(key)) {
            workofartModel = map.get(key);
        }
        else {
            workofartModel = null;
        }

        return workofartModel;
    }
}