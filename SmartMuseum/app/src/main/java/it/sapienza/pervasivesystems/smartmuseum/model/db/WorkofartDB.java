package it.sapienza.pervasivesystems.smartmuseum.model.db;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.business.aws.AWSConfiguration;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
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
            String cypher = "MATCH (e:Exhibit {beaconMajor: '" + em.getBeaconMajor() + "', beaconMinor:'" + em.getBeaconMinor() + "'}) <- [r:BELONGS_TO] - (woa:Workofart) return woa";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            WorkofartModel workofartModel = null;
            ExhibitBusiness exhibitBusiness = new ExhibitBusiness();
            for (CypherRow<List<Object>> row: rows) {
                workofartModel = this.readWorkofart(row);
                hashMapWorkofarts.put(this.getWorkofartHashmapKey(this.exhibitDB.getExhibitHashmapKey(em), workofartModel), workofartModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            hashMapWorkofarts = null;
        }

        return hashMapWorkofarts;
    }

    public String getWorkofartHashmapKey(String exhibitKey, WorkofartModel wam) {
        return exhibitKey.concat(new Integer(wam.getIdWork()).toString());
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
}
