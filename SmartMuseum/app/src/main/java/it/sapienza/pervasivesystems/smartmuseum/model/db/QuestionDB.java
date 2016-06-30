package it.sapienza.pervasivesystems.smartmuseum.model.db;

import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.business.datetime.DateTimeBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.QuestionModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRow;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.wsinterface.WSOperations;

/**
 * Created by andrearanieri on 03/06/16.
 */
public class QuestionDB {
    private WSOperations wsOperations = new WSOperations();

    public boolean insertQuestion(Date timeStamp, QuestionModel qm, UserModel um) {
        boolean result = false;
        try {
            result = this.wsOperations.getCypherResponse(this.createCypherInsertQuestion(timeStamp, qm, um));
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    }

    public List<QuestionModel> getQuestionsByUser(UserModel userModel) {
        List<QuestionModel> questions = new ArrayList<QuestionModel>();

        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH(q:Question) <- [r:POSTED] - (u:User {email:'" + userModel.getEmail() + "'}) RETURN q";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            QuestionModel questionModel = null;
            for (CypherRow<List<Object>> row: rows) {
                questionModel = this.readQuestion(row);
                questions.add(questionModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            questions = null;
        }

        return questions;
    }

    private String createCypherInsertQuestion(Date ts, QuestionModel qm, UserModel um){
        DateTimeBusiness dateTimeBusiness = new DateTimeBusiness(ts);
        long timeStamp = dateTimeBusiness.getMillis();
        String cypher = "MERGE(q:Question " +
                "{title: '" + qm.getTitle() + "', " +
                "channelName: '" + qm.getChannelName() + "', " +
                "timestamp:" + timeStamp + ", " +
                "open:1})\n" +
                "WITH q\n" +
                "MATCH (u:User {email:'" + um.getEmail() + "'})\n" +
                "MERGE (u)-[:POSTED]->(q)";
        Log.i("CYPHER", cypher);
        return cypher;
    }

    private QuestionModel readQuestion(CypherRow row) {
        QuestionModel questionModel = new QuestionModel();
        LinkedTreeMap<String, Object> objectMap = ((ArrayList<LinkedTreeMap<String, Object>>) row.getRow()).get(0);
        questionModel.setChannelName((String) (objectMap.get("channelName")));
        questionModel.setTitle((String) objectMap.get("title"));
        questionModel.setTimeStamp(DateTimeBusiness.getDateFromMillis(Math.round((Double) objectMap.get("timestamp"))));
        questionModel.setOpen(((Double) objectMap.get("open")) == 1 ? true : false);

        return questionModel;
    }

    }
