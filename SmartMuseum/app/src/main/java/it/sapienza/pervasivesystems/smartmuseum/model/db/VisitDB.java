package it.sapienza.pervasivesystems.smartmuseum.model.db;

import android.util.Log;

import java.util.Date;

import it.sapienza.pervasivesystems.smartmuseum.business.datetime.DateTimeBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.wsinterface.WSOperations;

/**
 * Created by andrearanieri on 04/05/16.
 */
public class VisitDB {
    private WSOperations wsOperations = new WSOperations();

    public boolean insertExhibitVisit(ExhibitModel em, UserModel um) {
        boolean result = false;
        try {
            result = this.wsOperations.getCypherResponse(this.createCypherInsertExhibitVisit(em, um));
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean insertWorkofartVisit(WorkofartModel wm, UserModel um) {
        boolean result = false;
        try {
            result = this.wsOperations.getCypherResponse(this.createCypherInsertWorkofartVisit(wm, um));
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    }

    private String createCypherInsertExhibitVisit(ExhibitModel em, UserModel um){
        DateTimeBusiness dateTimeBusiness = new DateTimeBusiness(new Date());
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
                "MERGE (u)-[:DID]->(v)\n" +
                "MERGE (v)-[:WHAT_EXHIBIT]->(e)";

        Log.i("CYPHER", cypher);

        return cypher;
    }

    private String createCypherInsertWorkofartVisit(WorkofartModel wm, UserModel um){
        DateTimeBusiness dateTimeBusiness = new DateTimeBusiness(new Date());
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
                "MATCH (w:Workofart {id:" + wm.getId() + "})\n" +
                "MERGE (u)-[:DID]->(v)\n" +
                "MERGE (v)-[:WHAT_WORKOFART]->(w)";
        return cypher;
    }
}
