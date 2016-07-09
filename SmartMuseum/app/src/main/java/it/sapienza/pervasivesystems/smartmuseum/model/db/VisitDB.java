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
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitWorkofartModel;
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

    public boolean insertWorkofartVisit(Date timeStamp, WorkofartModel wm, ExhibitModel em, UserModel um) {
        boolean result = false;
        try {
            result = this.wsOperations.getCypherResponse(this.createCypherInsertWorkofartVisit(timeStamp, wm, em, um));
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    }

    //returns an hashmap with the today user history;
    public HashMap<String, VisitExhibitModel> getTodayUserExhibitHistoryHashMap(UserModel um) {
        HashMap<String, VisitExhibitModel> userExhibitHistory = new HashMap<String, VisitExhibitModel>();
        List<CypherRow<List<Object>>> rows = null;
        DateTimeBusiness dateTimeBusiness = new DateTimeBusiness(new Date());
        try {
            String cypher = "MATCH\n" +
                    "(y:Year {id:" + dateTimeBusiness.getYear() + "}) <- [:PART_OF] - \n" +
                    "(m:Month {id:" + dateTimeBusiness.getMonth() + "}) <- [:PART_OF] - \n" +
                    "(d:Day {id:" + dateTimeBusiness.getDayOfMonth() + "}) <- [:PART_OF] - \n" +
                    "(h:Hour) <- [:PART_OF] - \n" +
                    "(mm:Minute) <- [:PART_OF] - \n" +
                    "(s:Second) <- [:HAPPENED_AT] - \n" +
                    "(v:Visit {type: 'exhibit'}) - [:WHAT_EXHIBIT] -> \n" +
                    "(e:Exhibit) \n" +
                    "WITH v, e\n" +
                    "MATCH \n" +
                    "(u:User {email: '" + um.getEmail() + "'}) - [:VISITED] -> (v)\n" +
                    "RETURN e, v";
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

    //returns an hashmap with the whole user exhibit history;
    public HashMap<String, VisitExhibitModel> getUserExhibitHistoryHashMap(UserModel um) {
        HashMap<String, VisitExhibitModel> userExhibitHistory = new HashMap<String, VisitExhibitModel>();
        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH (u: User {email:'" + um.getEmail() + "'}) - [r: VISITED] -> (v:Visit {type:'exhibit'}) - [ve: WHAT_EXHIBIT] -> (e) RETURN e, v";
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

    public HashMap<String, VisitWorkofartModel> getTodayUserWorkofartHistoryHashMap(UserModel um) {
        HashMap<String, VisitWorkofartModel> userExhibitHistory = new HashMap<String, VisitWorkofartModel>();
        List<CypherRow<List<Object>>> rows = null;
        DateTimeBusiness dateTimeBusiness = new DateTimeBusiness(new Date());
        try {
            String cypher = "MATCH\n" +
            "(y:Year {id:" + dateTimeBusiness.getYear() + "}) <- [:PART_OF] - \n" +
                    "(m:Month {id:" + dateTimeBusiness.getMonth() + "}) <- [:PART_OF] - \n" +
                    "(d:Day {id:" + dateTimeBusiness.getDayOfMonth() + "}) <- [:PART_OF] - \n" +
                    "(h:Hour) <- [:PART_OF] - \n" +
                    "(mm:Minute) <- [:PART_OF] - \n" +
                    "(s:Second) <- [:HAPPENED_AT] - \n" +
                    "(v:Visit {type: 'Workofart'}) - [:WHAT_WORKOFART] -> \n" +
                    "(w:Workofart) \n" +
                    "WITH v, w\n" +
                    "MATCH \n" +
                    "(u:User {email: '" + um.getEmail() + "'}) - [:VISITED] -> (v)\n" +
                    "RETURN w, v";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            VisitWorkofartModel visitWorkofartModel = null;
            for (CypherRow<List<Object>> row: rows) {
                visitWorkofartModel = this.readWorkofartVisit(row);
                userExhibitHistory.put(visitWorkofartModel.getWorkofartModel().getIdWork()+"", visitWorkofartModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            userExhibitHistory = null;
        }

        return userExhibitHistory;
    }

    public HashMap<String, VisitWorkofartModel> getTotalUserWorkofartHistoryHashMap(UserModel um) {
        HashMap<String, VisitWorkofartModel> userExhibitHistory = new HashMap<String, VisitWorkofartModel>();
        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH (u: User {email:'" + um.getEmail() + "'}) - [r: VISITED] -> (v:Visit {type:'Workofart'}) - [vw: WHAT_WORKOFART] -> (w) RETURN w, v";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            VisitWorkofartModel visitWorkofartModel = null;
            for (CypherRow<List<Object>> row: rows) {
                visitWorkofartModel = this.readWorkofartVisit(row);
                userExhibitHistory.put(visitWorkofartModel.getWorkofartModel().getIdWork()+"", visitWorkofartModel);
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

    private String createCypherInsertWorkofartVisit(Date ts, WorkofartModel wm, ExhibitModel em, UserModel um){
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
                "MERGE (s)<-[:HAPPENED_AT]-(v: Visit {type:'Workofart',timestamp:'" + timeStamp + "'})\n" +
                "WITH v\n" +
                "MATCH (u:User {email:'" + um.getEmail() + "'})\n" +
                "MATCH (e:Exhibit {beaconMajor:'" + em.getBeaconMajor() + "', beaconMinor:'" + em.getBeaconMinor() + "'})\n" +
                "MATCH (w:Workofart {idWork:'" + wm.getIdWork() + "'}) - [:BELONGS_TO] -> (e)\n" +
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
        exhibit.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("audio"))));

        visitExhibitModel.setExhibitModel(exhibit);

        LinkedTreeMap<String, String> objectMap2 = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(1);
        visitExhibitModel.setTimeStamp(DateTimeBusiness.getDateFromMillis(Long.parseLong(objectMap2.get("timestamp"))));
        return visitExhibitModel;
    }

    private VisitWorkofartModel readWorkofartVisit(CypherRow row) {
        VisitWorkofartModel visitWorkofartModel = new VisitWorkofartModel();
        LinkedTreeMap<String, String> objectMap = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(0);
        WorkofartModel workofartModel = new WorkofartModel();
        workofartModel.setIdWork(Integer.parseInt((objectMap.get("idWork"))));
        workofartModel.setTitle(objectMap.get("title"));
        workofartModel.setShortDescription(objectMap.get("shortDescription"));
        workofartModel.setLongDescription("");
        workofartModel.setLongDescriptionURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("longDescription"))));
        workofartModel.setImage("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("image"))));
        workofartModel.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("audio"))));
        visitWorkofartModel.setWorkofartModel(workofartModel);

        LinkedTreeMap<String, String> objectMap2 = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(1);
        visitWorkofartModel.setTimestamp(DateTimeBusiness.getDateFromMillis(Long.parseLong(objectMap2.get("timestamp"))));
        return visitWorkofartModel;
    }
}
