package it.sapienza.pervasivesystems.smartmuseum.model.db;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.business.aws.AWSConfiguration;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRow;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.wsinterface.WSOperations;

/**
 * Created by andrearanieri on 01/05/16.
 */
public class ExhibitDB {

    private WSOperations wsOperations = new WSOperations();

    public HashMap<String, ExhibitModel> getExhibitsFromDB2() {
        HashMap<String, ExhibitModel> hashMapExhibits = new HashMap<String, ExhibitModel>();
        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH (e:Exhibit) return e";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            ExhibitModel exhibitModel = null;
            ExhibitBusiness exhibitBusiness = new ExhibitBusiness();
            for (CypherRow<List<Object>> row: rows) {
                exhibitModel = this.readExhibit(row);
                hashMapExhibits.put(exhibitBusiness.getExhibitHashmapKey(exhibitModel), exhibitModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            hashMapExhibits = null;
        }

        return hashMapExhibits;
    }

    public HashMap<String, ExhibitModel> getTodayUserExhibitHistory(UserModel um) {
        HashMap<String, ExhibitModel> userExhibitHistory = new HashMap<String, ExhibitModel>();
        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH (u: User {email:'" + um.getEmail() + "'}) - [r: VISITED] -> (v:Visit {type:'exhibit'}) - [ve: WHAT_EXHIBIT] -> (e) RETURN e";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            ExhibitModel exhibitModel = null;
            for (CypherRow<List<Object>> row: rows) {
                exhibitModel = this.readExhibit(row);
                userExhibitHistory.put(this.getExhibitHashmapKey(exhibitModel), exhibitModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            userExhibitHistory = null;
        }

        return userExhibitHistory;
    }

    public ArrayList<ExhibitModel> getUserExhibitHistory(UserModel um) {
        ArrayList<ExhibitModel> userExhibitHistory = new ArrayList<ExhibitModel>();
        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH (u: User {email:'" + um.getEmail() + "'}) - [r: VISITED] -> (v:Visit {type:'exhibit'}) - [ve: WHAT_EXHIBIT] -> (e) RETURN e";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            ExhibitModel exhibitModel = null;
            for (CypherRow<List<Object>> row: rows) {
                exhibitModel = this.readExhibit(row);
                userExhibitHistory.add(exhibitModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            userExhibitHistory = null;
        }

        return userExhibitHistory;
    }

    //key used to retrieve exhibits from the application level hashmap that contains all of them;
    public String getExhibitHashmapKey(ExhibitModel em) {
        return em.getBeaconMajor().concat(":").concat(em.getBeaconMinor());
    }

    private ExhibitModel readExhibit(CypherRow row) {
        LinkedTreeMap<String, String> objectMap = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(0);
        ExhibitModel exhibit = new ExhibitModel();
        exhibit.setTitle(objectMap.get("title"));
        exhibit.setShortDescription(objectMap.get("shortDescription"));
        exhibit.setLongDescription(objectMap.get("longDescription"));
        exhibit.setLongDescriptionURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("longDescription"))));
        exhibit.setImage("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("image"))));
        exhibit.setLocation(objectMap.get("location"));
        exhibit.setOpeningHour(objectMap.get("openingHour"));
        exhibit.setPeriod(objectMap.get("period"));
        exhibit.setBeaconProximityUUID(objectMap.get("beaconProximityUUID"));
        exhibit.setBeaconMajor(objectMap.get("beaconMajor"));
        exhibit.setBeaconMinor(objectMap.get("beaconMinor"));
        exhibit.setBeacon(objectMap.get("beacon"));
        exhibit.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("audio"))));
        return exhibit;
    }
}
