package it.sapienza.pervasivesystems.smartmuseum.model.db;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.business.aws.AWSConfiguration;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.WorkofartBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRow;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.wsinterface.WSOperations;

/**
 * Created by andrearanieri on 07/05/16.
 */
public class WorkofartDB {
    private WSOperations wsOperations = new WSOperations();
    private WorkofartBusiness workofartBusiness = new WorkofartBusiness();

    public HashMap<String, WorkofartModel> getWorkofarts(ExhibitModel exhibit) {
        HashMap<String, WorkofartModel> hashMapWorkofarts = new HashMap<String, WorkofartModel>();
        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH (e:Exhibit) return e";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            WorkofartModel workofartModel = null;
            ExhibitBusiness exhibitBusiness = new ExhibitBusiness();
            for (CypherRow<List<Object>> row: rows) {
                workofartModel = this.readWorkofart(row);
                hashMapWorkofarts.put(this.workofartBusiness.getWorkofartHashmapKey(exhibit, workofartModel), workofartModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            hashMapWorkofarts = null;
        }

        return hashMapWorkofarts;
    }

    private WorkofartModel readWorkofart(CypherRow row) {
        LinkedTreeMap<String, String> objectMap = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(0);
        WorkofartModel workofartModel = new WorkofartModel();
        workofartModel.setTitle(objectMap.get("title"));
        workofartModel.setShortDescription(objectMap.get("shortDescription"));
        workofartModel.setLongDescription("");
        workofartModel.setLongDescriptionURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("longDescription"))));
        workofartModel.setImage("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("image"))));
        workofartModel.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("audio"))));
        return workofartModel;
    }
}
