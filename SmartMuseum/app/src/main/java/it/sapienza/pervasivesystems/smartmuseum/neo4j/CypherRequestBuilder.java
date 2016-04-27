package it.sapienza.pervasivesystems.smartmuseum.neo4j;

import com.google.common.base.Joiner;

import java.util.Map;

/**
 * CypherRequest Builder object
 *
 * @See CypherRequest.Builder()
 */
public class CypherRequestBuilder {

    private CypherRequest template = new CypherRequest();

    /**
     * Package scope constructor as exposed via CypherRequest.Builder()
     */
    CypherRequestBuilder() {
    }

    private String join(String joinwith, String... args) {
        return Joiner.on(",").join(args);
    }

    public CypherRequestBuilder withStatement(String statement) {
        this.template.statements.add(new CypherStatement(statement));
        return this;
    }

    public CypherRequestBuilder withStatement(String statement, Map<String, Object> parameters) {
        this.template.statements.add(new CypherStatement(statement, parameters));
        return this;
    }

    public CypherRequestBuilder withStatement(CypherStatement statement) {
        this.template.statements.add(statement);
        return this;
    }

    public CypherRequest build() {
        // TODO clone this
        return this.template;
    }
}
