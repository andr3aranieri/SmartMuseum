package it.sapienza.pervasivesystems.smartmuseum.api.cyphers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andrearanieri on 25/04/16.
 */
public class QueryCypher {
    @SerializedName("query")
    private String query;

    public QueryCypher(String q) {
        this.query = q;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}

