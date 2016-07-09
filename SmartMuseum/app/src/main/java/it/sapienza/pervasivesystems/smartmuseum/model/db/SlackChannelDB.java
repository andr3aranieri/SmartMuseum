package it.sapienza.pervasivesystems.smartmuseum.model.db;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.model.entity.SlackChannelModel;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRow;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.wsinterface.WSOperations;

/**
 * Created by andrearanieri on 05/07/16.
 */
public class SlackChannelDB {

    private WSOperations wsOperations = new WSOperations();

    public HashMap<String, SlackChannelModel> getTakenChannels() {
        HashMap<String, SlackChannelModel> channels = new HashMap<String, SlackChannelModel>();
        List<CypherRow<List<Object>>> rows = null;

        try {
            String cypher = "MATCH (c:Channel) return c";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            SlackChannelModel slackChannelModel = null;
            for (CypherRow<List<Object>> row: rows) {
                slackChannelModel = this.readSlackChannel(row);
                channels.put(slackChannelModel.getChannelId(), slackChannelModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            channels = null;
        }
        return channels;
    }

    private SlackChannelModel readSlackChannel(CypherRow row) {
        LinkedTreeMap<String, String> objectMap = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(0);
        SlackChannelModel channel = new SlackChannelModel();
        channel.setChannelId(objectMap.get("channelid"));
        channel.setChannelName(objectMap.get("channelname"));
        return channel;
    }
}
