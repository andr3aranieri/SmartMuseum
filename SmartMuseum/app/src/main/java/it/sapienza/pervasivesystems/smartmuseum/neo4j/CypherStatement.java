package it.sapienza.pervasivesystems.smartmuseum.neo4j;

import java.util.HashMap;
import java.util.Map;

/**
 * An object to represent a Cypher Statement
 */
public class CypherStatement {

    public static CypherStatementBuilder Builder() {
        return new CypherStatementBuilder();
    }

    final String statement;
    final Map<String, Object> parameters = new HashMap<>();

    public CypherStatement(String statement) {
        this(statement, new HashMap<String, Object>());
    }

    public CypherStatement(String statement, Map<String, Object> parameters) {
        this.statement = statement;
        this.parameters.putAll(parameters);
    }

    @Override
    public String toString() {
        return CypherRequest.GSON.toJson(this);
    }

    public String getStatement() {
        return statement;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
}
