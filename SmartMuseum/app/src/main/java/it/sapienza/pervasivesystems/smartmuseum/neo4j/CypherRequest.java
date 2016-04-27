package it.sapienza.pervasivesystems.smartmuseum.neo4j;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * An object to help create a json CypherQuery
 */
public class CypherRequest {

    static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    public static CypherRequestBuilder Builder() {
        return new CypherRequestBuilder();
    }

    List<CypherStatement> statements = new ArrayList<>();

    @Override
    public String toString() {
        return GSON.toJson(this);
    }

    public List<CypherStatement> getStatements() {
        return statements;
    }
}
