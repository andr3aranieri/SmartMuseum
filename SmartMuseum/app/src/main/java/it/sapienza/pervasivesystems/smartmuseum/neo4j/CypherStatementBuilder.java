package it.sapienza.pervasivesystems.smartmuseum.neo4j;

import com.google.common.base.Joiner;

import java.util.HashMap;
import java.util.Map;

/**
 * CypherRequest Builder object
 *
 * @See CypherRequest.Builder()
 */
public class CypherStatementBuilder {

    private CypherStatement template = new CypherStatement("",new HashMap<String, Object>());
    private StringBuffer query = new StringBuffer();

    /**
     * Package scope constructor as exposed via CypherStatement.Builder()
     */
    CypherStatementBuilder() {
    }

    private String join(String joinwith, String... args) {
        return Joiner.on(",").join(args);
    }

    public CypherStatementBuilder withCypher(String cypher) {
        this.query.append(cypher).append(" ");
        return this;
    }

    public CypherStatementBuilder withParameterMap(Map<String, Object> queryParamsMap) {
        this.template.parameters.putAll(queryParamsMap);
        return this;
    }

    public CypherStatementBuilder withParameter(String name, Object value) {
        this.template.parameters.put(name, value);
        return this;
    }

    public CypherStatementBuilder MATCH(String... args) {
        this.query.append("MATCH ").append(join(", ", args)).append(" ");
        return this;
    }

    public CypherStatementBuilder WHERE(String... conditions) {
        this.query.append("WHERE ").append(join(", ", conditions)).append(" ");
        return this;
    }

    public CypherStatementBuilder RETURN(String... args) {
        this.query.append("RETURN ").append(join(", ", args)).append(" ");
        return this;
    }

    public CypherStatementBuilder LIMIT(long limit) {
        this.query.append("LIMIT ").append(limit).append(" ");
        return this;
    }

    public CypherStatement build() {
        CypherStatement result = new CypherStatement(this.query.toString().trim(), this.template.getParameters());
        return result;

    }
}
