package it.sapienza.pervasivesystems.smartmuseum.model.db;


import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

import it.sapienza.pervasivesystems.smartmuseum.business.cryptography.SHA1Business;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.SlackChannelModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRow;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.wsinterface.WSOperations;


/**
 * Created by andrearanieri on 25/04/16.
 */
public class UserDB extends DBManager {

    private WSOperations wsOperations = new WSOperations();

    public UserModel getUserByEmail(String email) {
        UserModel userRet = null;
        CypherRow row = null;

        try {
            String cypher = "MATCH (u:User { email: '" + email + "'}) - [r: WRITES_ON] -> (c:Channel) return u, c";
            row = this.wsOperations.getCypherSingleResult(cypher);
            if(row != null) {
                userRet = this.readUser(row);
                Log.i("GET USER RESULT: ", userRet.getEmail() + ", " + userRet.getName() + ", " + userRet.getPassword() + ", " + userRet.getProfileImage());
            }
            else {
                userRet = null;
                Log.i("GET USER RESULT: ", "NO DATA");
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            userRet = null;
        }

        return userRet;
    }

    public boolean createUser(UserModel user) {
        boolean ret = false;
        try {
            String cypher = "CREATE (n:User { name : '" + user.getName() + "', email : '" + user.getEmail() + "', password : '" + SHA1Business.SHA1(user.getPassword()) + "', profileImage : '" + user.getProfileImage() + "' })";
            ret = this.wsOperations.getCypherResponse(cypher);
            Log.i("CREATE USER RESULT: : ", new Boolean(ret).toString());
        }
        catch(Exception ex) {
            ex.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public boolean connectChannelToUser(UserModel user, SlackChannelModel channel) {
        boolean ret = false;
        try {
            String cypher = "MERGE (c:Channel {channelid:'" + channel.getChannelId() + "',channelname:'" + channel.getChannelName() + "'}) WITH c MATCH (u:User {email:'" + user.getEmail() + "'}) MERGE (u) - [:WRITES_ON] -> (c)";
            ret = this.wsOperations.getCypherResponse(cypher);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            ret = false;
        }
        return ret;
    }

    private UserModel readUser(CypherRow row) {
        LinkedTreeMap<String, String> objectMap = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(0);
        UserModel user = new UserModel();
        user.setName(objectMap.get("name"));
        user.setEmail(objectMap.get("email"));
        user.setPassword(objectMap.get("password"));
        user.setProfileImage(objectMap.get("profileImage"));

        LinkedTreeMap<String, String> objectMap2 = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(1);
        SlackChannelModel slackChannelModel = new SlackChannelModel();
        slackChannelModel.setChannelId(objectMap2.get("channelid"));
        slackChannelModel.setChannelName(objectMap2.get("channelname"));
        user.setSlackChannel(slackChannelModel);
        return user;
    }
}
