package it.sapienza.pervasivesystems.smartmuseum.business.exhibits;

import java.util.ArrayList;
import java.util.HashMap;

import it.sapienza.pervasivesystems.smartmuseum.model.db.WorkofartDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;

/**
 * Created by andrearanieri on 07/05/16.
 */
public class WorkofartBusiness {

    private WorkofartDB workofartDB = new WorkofartDB();

    public String getWorkofartHashmapKey(String exhibitKey, WorkofartModel wam) {
        return this.workofartDB.getWorkofartHashmapKey(exhibitKey, wam);
    }

    public HashMap<String, WorkofartModel> getWorkofarts(ExhibitModel exhibitModel) {
        return this.workofartDB.getWorkofarts(exhibitModel);
    }

    public ArrayList<WorkofartModel> getWorksofartList(HashMap<String, WorkofartModel> map) {
        return new ArrayList<WorkofartModel>(map.values());
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