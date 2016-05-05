package it.sapienza.pervasivesystems.smartmuseum.neo4j.wsinterface;

import org.apache.commons.httpclient.HttpStatus;

import java.io.IOException;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherNodeSet;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRequest;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherResultSet;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRow;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherService;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherStatement;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.Neo4jRestService;
import retrofit.client.Response;

/**
 * Created by andrearanieri on 27/04/16.
 */
public class WSOperations {
    CypherService dao = new CypherService();
    private Neo4jRestService neo4j = this.dao.getNeo4jRestService();

    public CypherRow getCypherSingleResult(String cypher) throws IOException {
        CypherStatement statement = CypherStatement.Builder().withCypher(cypher).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        CypherResultSet<List<Object>> result = this.neo4j.getCypherResultSet(request);
        CypherNodeSet<List<Object>> nodes = result.getResults().get(0);
        return nodes.getData() != null && nodes.getData().size() > 0 ? nodes.getData().get(0) : null;
    }

    public List<CypherRow<List<Object>>> getCypherMultipleResults(String cypher) throws IOException {
        CypherStatement statement = CypherStatement.Builder().withCypher(cypher).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        CypherResultSet<List<Object>> result = this.neo4j.getCypherResultSet(request);
        CypherNodeSet<List<Object>> nodes = result.getResults().get(0);
        return nodes.getData();
    }

    public boolean getCypherResponse(String cypher) throws  IOException {
        boolean success = false;
        CypherStatement statement = CypherStatement.Builder().withCypher(cypher).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        Response result = this.neo4j.getCypherResponse(request);
        success = result.getStatus() == HttpStatus.SC_OK;
        return success;
    }

}
