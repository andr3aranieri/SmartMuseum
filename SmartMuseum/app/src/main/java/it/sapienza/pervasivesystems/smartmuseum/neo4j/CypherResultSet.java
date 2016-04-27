package it.sapienza.pervasivesystems.smartmuseum.neo4j;

import java.util.List;
import java.util.Map;

/**
 * Cypher response object.
 */
public class CypherResultSet<T> {

    String commit;
    Map<String, Object> transaction;
    List<CypherNodeSet<T>> results;
    List<String> errors;

    public CypherResultSet(List<CypherNodeSet<T>> results) {
        this.results = results;
    }

    public CypherResultSet(String commit, Map<String, Object> transaction, List<CypherNodeSet<T>> results, List<String> errors) {
        this.commit = commit;
        this.transaction = transaction;
        this.results = results;
        this.errors = errors;
    }

    public List<CypherNodeSet<T>> getResults() {
        return results;
    }

    public void setResults(List<CypherNodeSet<T>> results) {
        this.results = results;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
