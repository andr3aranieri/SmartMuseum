package it.sapienza.pervasivesystems.smartmuseum.model.db;


import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

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
            String cypher = "MATCH (u:User { email: '" + email + "'}) return u";
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
            String cypher = "CREATE (n:User { name : '" + user.getName() + "', email : '" + user.getEmail() + "', password : '" + user.getPassword() + "', profileImage : '" + user.getProfileImage() + "' })";
            ret = this.wsOperations.getCypherResponse(cypher);
            Log.i("CREATE USER RESULT: : ", new Boolean(ret).toString());
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
        return user;
    }
}
