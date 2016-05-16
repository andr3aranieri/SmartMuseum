package it.sapienza.pervasivesystems.smartmuseum.model.db;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.business.aws.AWSConfiguration;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRow;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.wsinterface.WSOperations;

/**
 * Created by andrearanieri on 07/05/16.
 */
public class WorkofartDB {
    private WSOperations wsOperations = new WSOperations();
    private ExhibitDB exhibitDB = new ExhibitDB();

    public HashMap<String, WorkofartModel> getWorkofarts(ExhibitModel em) {
        HashMap<String, WorkofartModel> hashMapWorkofarts = new HashMap<String, WorkofartModel>();
        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH (e:Exhibit {beaconMajor: '" + em.getBeaconMajor() + "', beaconMinor:'" + em.getBeaconMinor() + "'}) <- [r:BELONGS_TO] - (woa:Workofart) return woa, e";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            WorkofartModel workofartModel = null;
            ExhibitBusiness exhibitBusiness = new ExhibitBusiness();
            for (CypherRow<List<Object>> row: rows) {
                workofartModel = this.readWorkofartWithExhibit(row);
                hashMapWorkofarts.put(this.getWorkofartHashmapKey(this.exhibitDB.getExhibitHashmapKey(em), workofartModel), workofartModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            hashMapWorkofarts = null;
        }

        return hashMapWorkofarts;
    }

    public HashMap<String, WorkofartModel> getTodayUserWorkofartHistory(UserModel um) {
        HashMap<String, WorkofartModel> userWorksofartHistory = new HashMap<String, WorkofartModel>();
        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH (u: User {email:'" + um.getEmail() + "'}) - [r: VISITED] -> (v:Visit {type:'Workofart'}) - [vw: WHAT_WORKOFART] -> (w) - [we: BELONGS_TO] - >(e) RETURN w, e";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            WorkofartModel workofartModel = null;
            for (CypherRow<List<Object>> row: rows) {
                workofartModel = this.readWorkofartWithExhibit(row);
                userWorksofartHistory.put(this.getWorkofartHashmapKey(this.exhibitDB.getExhibitHashmapKey(workofartModel.getExhibitModel()), workofartModel), workofartModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            userWorksofartHistory = null;
        }

        return userWorksofartHistory;
    }

    public ArrayList<WorkofartModel> getUserWorkofartHistory(UserModel um) {
        ArrayList<WorkofartModel> userWorksofartHistory = new ArrayList<WorkofartModel>();
        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH (u: User {email:'" + um.getEmail() + "'}) - [r: VISITED] -> (v:Visit {type:'Workofart'}) - [vw: WHAT_WORKOFART] -> (w) - [we: BELONGS_TO] - >(e) RETURN w, e";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            WorkofartModel workofartModel = null;
            for (CypherRow<List<Object>> row: rows) {
                workofartModel = this.readWorkofartWithExhibit(row);
                userWorksofartHistory.add(workofartModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            userWorksofartHistory = null;
        }

        return userWorksofartHistory;
    }


    public String getWorkofartHashmapKey(String exhibitKey, WorkofartModel wam) {
        return exhibitKey.concat(":").concat(new Integer(wam.getIdWork()).toString());
    }

    private WorkofartModel readWorkofart(CypherRow row) {
        LinkedTreeMap<String, String> objectMap = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(0);
        WorkofartModel workofartModel = new WorkofartModel();
        workofartModel.setIdWork(Integer.parseInt((objectMap.get("idWork"))));
        workofartModel.setTitle(objectMap.get("title"));
        workofartModel.setShortDescription(objectMap.get("shortDescription"));
        workofartModel.setLongDescription("");
        workofartModel.setLongDescriptionURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("longDescription"))));
        workofartModel.setImage("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("image"))));
        workofartModel.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("audio"))));
        return workofartModel;
    }

    private WorkofartModel readWorkofartWithExhibit(CypherRow row) {
        LinkedTreeMap<String, String> objectMap = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(0);
        WorkofartModel workofartModel = new WorkofartModel();
        workofartModel.setIdWork(Integer.parseInt((objectMap.get("idWork"))));
        workofartModel.setTitle(objectMap.get("title"));
        workofartModel.setShortDescription(objectMap.get("shortDescription"));
        workofartModel.setLongDescription(objectMap.get("longDescription"));
        workofartModel.setLongDescriptionURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("longDescription"))));
        workofartModel.setImage("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("image"))));
        workofartModel.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("audio"))));

        LinkedTreeMap<String, String> objectMap2 = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(1);
        ExhibitModel exhibit = new ExhibitModel();
        exhibit.setTitle(objectMap2.get("title"));
        exhibit.setShortDescription(objectMap2.get("shortDescription"));
        exhibit.setLongDescriptionURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap2.get("longDescription"))));
        exhibit.setImage("https://".concat(AWSConfiguration.awsHostname.concat(objectMap2.get("image"))));
        exhibit.setLocation(objectMap2.get("location"));
        exhibit.setOpeningHour(objectMap2.get("openingHour"));
        exhibit.setPeriod(objectMap2.get("period"));
        exhibit.setBeaconProximityUUID(objectMap2.get("beaconProximityUUID"));
        exhibit.setBeaconMajor(objectMap2.get("beaconMajor"));
        exhibit.setBeaconMinor(objectMap2.get("beaconMinor"));
        exhibit.setBeacon(objectMap2.get("beacon"));
        exhibit.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap2.get("audio"))));
        workofartModel.setExhibitModel(exhibit);

        return workofartModel;
    }

}
