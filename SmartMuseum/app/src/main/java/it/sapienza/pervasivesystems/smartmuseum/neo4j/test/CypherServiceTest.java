package it.sapienza.pervasivesystems.smartmuseum.neo4j.test;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Test;

import java.io.IOException;

import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherResultSet;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherService;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class CypherServiceTest {

    CypherService graph = new CypherService();

    @Test
    public void getStatus() throws IOException {
        assertThat(graph.getServerStatus(), is(HttpStatus.SC_OK));
    }

    @Test
    public void getSomeData() throws IOException {
        CypherResultSet<Object> response = graph.runCypher("MATCH (n) RETURN n LIMIT 10");
        assertThat(response.getResults().get(0).getData().size(), is(10));
    }
}
