package it.sapienza.pervasivesystems.smartmuseum.model.db;

import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.business.aws.AWSConfiguration;
import it.sapienza.pervasivesystems.smartmuseum.business.datetime.DateTimeBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRow;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.wsinterface.WSOperations;

/**
 * Created by andrearanieri on 04/05/16.
 */
public class VisitDB {
    private WSOperations wsOperations = new WSOperations();
    private ExhibitDB exhibitDB = new ExhibitDB();

    public boolean insertExhibitVisit(Date timeStamp, ExhibitModel em, UserModel um) {
        boolean result = false;
        try {
            result = this.wsOperations.getCypherResponse(this.createCypherInsertExhibitVisit(timeStamp, em, um));
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
            }
        return result;
    }

    public boolean insertWorkofartVisit(Date timeStamp, WorkofartModel wm, UserModel um) {
        boolean result = false;
        try {
            result = this.wsOperations.getCypherResponse(this.createCypherInsertWorkofartVisit(timeStamp, wm, um));
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    }

    public HashMap<String, VisitExhibitModel> getUserExhibitHistory(UserModel um) {
        HashMap<String, VisitExhibitModel> userExhibitHistory = new HashMap<String, VisitExhibitModel>();
        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH (u: User {email:'" + um.getEmail() + "'}) - [r: VISITED] -> (v:Visit {type:'exhibit'}) - [ve: WHAT_EXHIBIT] -> (e) RETURN e, v.timestamp";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            VisitExhibitModel visitExhibitModel = null;
            for (CypherRow<List<Object>> row: rows) {
                visitExhibitModel = this.readVisitExhibit(row);
                userExhibitHistory.put(this.exhibitDB.getExhibitHashmapKey(visitExhibitModel.getExhibitModel()), visitExhibitModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            userExhibitHistory = null;
        }

        return userExhibitHistory;
    }

    public HashMap<String, VisitExhibitModel> getTodayUserExhibitHistory(UserModel um) {
        HashMap<String, VisitExhibitModel> userExhibitHistory = new HashMap<String, VisitExhibitModel>();
        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH (u: User {email:'" + um.getEmail() + "'}) - [r: VISITED] -> (v:Visit {type:'exhibit'}) - [ve: WHAT_EXHIBIT] -> (e) RETURN e, v.timestamp";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            VisitExhibitModel visitExhibitModel = null;
            for (CypherRow<List<Object>> row: rows) {
                visitExhibitModel = this.readVisitExhibit(row);
                userExhibitHistory.put(this.exhibitDB.getExhibitHashmapKey(visitExhibitModel.getExhibitModel()), visitExhibitModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            userExhibitHistory = null;
        }

        return userExhibitHistory;
    }

    private String createCypherInsertExhibitVisit(Date ts, ExhibitModel em, UserModel um){
        DateTimeBusiness dateTimeBusiness = new DateTimeBusiness(ts);
        int year = dateTimeBusiness.getYear(),
                month = dateTimeBusiness.getMonth(),
                day = dateTimeBusiness.getDayOfMonth(),
                hour = dateTimeBusiness.getHour(),
                minute = dateTimeBusiness.getMinute(),
                second = dateTimeBusiness.getSecond();
        long timeStamp = dateTimeBusiness.getMillis();
        String cypher = "MERGE (y:Year {id:" + year + "})\n" +
                "MERGE (y)<-[:PART_OF]-(m:Month {id:" + month + "})\n" +
                "MERGE (m)<-[:PART_OF]-(d:Day {id:" + day + "})\n" +
                "MERGE (d)<-[:PART_OF]-(h:Hour {id:" + hour + "})\n" +
                "MERGE (h)<-[:PART_OF]-(min:Minute {id:" + minute + "})\n" +
                "MERGE (min)<-[:PART_OF]-(s:Second {id:" + second + "})\n" +
                "MERGE (s)<-[:HAPPENED_AT]-(v: Visit {type:'exhibit',timestamp:'" + timeStamp + "'})\n" +
                "WITH v\n" +
                "MATCH (u:User {email:'" + um.getEmail() + "'})\n" +
                "MATCH (e:Exhibit {beaconMajor:'" + em.getBeaconMajor() + "', beaconMinor:'" + em.getBeaconMinor() + "'})\n" +
                "MERGE (u)-[:VISITED]->(v)\n" +
                "MERGE (v)-[:WHAT_EXHIBIT]->(e)";

        Log.i("CYPHER", cypher);

        return cypher;
    }

    private String createCypherInsertWorkofartVisit(Date ts, WorkofartModel wm, UserModel um){
        DateTimeBusiness dateTimeBusiness = new DateTimeBusiness(ts);
        int year = dateTimeBusiness.getYear(),
                month = dateTimeBusiness.getMonth(),
                day = dateTimeBusiness.getDayOfMonth(),
                hour = dateTimeBusiness.getHour(),
                minute = dateTimeBusiness.getMinute(),
                second = dateTimeBusiness.getSecond();
        long timeStamp = dateTimeBusiness.getMillis();
        String cypher = "MERGE (y:Year {id:" + year + "})\n" +
                "MERGE (y)<-[:PART_OF]-(m:Month {id:" + month + "})\n" +
                "MERGE (m)<-[:PART_OF]-(d:Day {id:" + day + "})\n" +
                "MERGE (d)<-[:PART_OF]-(h:Hour {id:" + hour + "})\n" +
                "MERGE (h)<-[:PART_OF]-(min:Minute {id:" + minute + "})\n" +
                "MERGE (min)<-[:PART_OF]-(s:Second {id:" + second + "})\n" +
                "MERGE (s)<-[:HAPPENED_AT]-(v: Visit {type:'exhibit',timestamp:'" + timeStamp + "'})\n" +
                "WITH v\n" +
                "MATCH (u:User {email:'" + um.getEmail() + "'})\n" +
                "MATCH (w:Workofart {id:" + wm.getIdWork() + "})\n" +
                "MERGE (u)-[:VISITED]->(v)\n" +
                "MERGE (v)-[:WHAT_WORKOFART]->(w)";
        return cypher;
    }

    private VisitExhibitModel readVisitExhibit(CypherRow row) {
        LinkedTreeMap<String, String> objectMap = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(0);
        VisitExhibitModel visitExhibitModel = new VisitExhibitModel();
        ExhibitModel exhibit = new ExhibitModel();
        exhibit.setTitle(objectMap.get("title"));
        exhibit.setShortDescription(objectMap.get("shortDescription"));
//        exhibit.setLongDescriptionURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("e.longDescription"))));
        exhibit.setLongDescription(objectMap.get("longDescription"));
        exhibit.setImage("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("image"))));
        exhibit.setLocation(objectMap.get("location"));
        exhibit.setOpeningHour(objectMap.get("openingHour"));
        exhibit.setPeriod(objectMap.get("period"));
        exhibit.setBeaconProximityUUID(objectMap.get("beaconProximityUUID"));
        exhibit.setBeaconMajor(objectMap.get("beaconMajor"));
        exhibit.setBeaconMinor(objectMap.get("beaconMinor"));
        exhibit.setBeacon(objectMap.get("beacon"));

        visitExhibitModel.setExhibitModel(exhibit);
        visitExhibitModel.setTimeStamp(DateTimeBusiness.getDateFromMillis(Long.parseLong(objectMap.get("v.timestamp"))));
        return visitExhibitModel;
    }
}
