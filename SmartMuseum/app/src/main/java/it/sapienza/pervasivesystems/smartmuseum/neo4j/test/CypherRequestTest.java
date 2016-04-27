package it.sapienza.pervasivesystems.smartmuseum.neo4j.test;

import org.junit.Test;

import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRequest;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherStatement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class CypherRequestTest {

    /**
     * Tests CypherQuery with simple pre-created Cypher string.
     */
    @Test
    public void testCypherRequest() {


        CypherRequest raw = CypherRequest.Builder().withStatement("MATCH n RETURN n LIMIT 10").build();
        assertThat(raw.getStatements().get(0).getStatement(), is("MATCH n RETURN n LIMIT 10"));
        assertThat(raw.toString(), is("{\"statements\":[{\"statement\":\"MATCH n RETURN n LIMIT 10\",\"parameters\":{}}]}"));

        CypherStatement statement = CypherStatement.Builder()
                .MATCH("n")
                .RETURN("n")
                .LIMIT(10)
                .build();
        CypherRequest request = CypherRequest.Builder()
                .withStatement(statement)
                .build();

        assertThat(request.getStatements().get(0).getStatement(), is("MATCH n RETURN n LIMIT 10"));
        assertThat(request.toString(), is(raw.toString()));
    }

    /**
     * Tests CypherQuery with simple Cypher string construction.
     */
    @Test
    public void testQueryMovieAndDirectorsObjects() {

        String queryMovieAndDirectorsObjects = "MATCH (m:Movie)-[r:DIRECTED]-(a:Person) WHERE m.title = 'The Matrix' RETURN m,r,a";
        CypherRequest raw = CypherRequest.Builder().withStatement(queryMovieAndDirectorsObjects).build();
        assertThat(raw.getStatements().get(0).getStatement(), is(queryMovieAndDirectorsObjects));
        assertThat(raw.toString(), is("{\"statements\":[{\"statement\":\"" + queryMovieAndDirectorsObjects + "\",\"parameters\":{}}]}"));

        CypherStatement statement = CypherStatement.Builder()
                .MATCH("(m:Movie)-[r:DIRECTED]-(a:Person)")
                .WHERE("m.title = 'The Matrix'")
                .RETURN("m,r,a")
                .build();
        CypherRequest request = CypherRequest.Builder()
                .withStatement(statement)
                .build();
        assertThat(request.getStatements().get(0).getStatement(), is(queryMovieAndDirectorsObjects));
        assertThat(request.toString(), is(raw.toString()));
    }


    /**
     * Tests CypherQuery with simple Cypher string construction.
     */
    @Test
    public void testQueryMovieAndDirectorsColumns() {

        String queryMovieAndDirectorsColumns = "MATCH (m:Movie)-[r:DIRECTED]-(a:Person) WHERE m.title = 'The Matrix' RETURN m.title,r.Type,a.cypher,a.born";
        CypherRequest raw = CypherRequest.Builder().withStatement(queryMovieAndDirectorsColumns).build();
        assertThat(raw.getStatements().get(0).getStatement(), is(queryMovieAndDirectorsColumns));
        assertThat(raw.toString(), is("{\"statements\":[{\"statement\":\"" + queryMovieAndDirectorsColumns + "\",\"parameters\":{}}]}"));

        CypherStatement statement = CypherStatement.Builder()
                .MATCH("(m:Movie)-[r:DIRECTED]-(a:Person)")
                .WHERE("m.title = 'The Matrix'")
                .RETURN("m.title,r.Type,a.cypher,a.born")
                .build();
        CypherRequest request = CypherRequest.Builder()
                .withStatement(statement)
                .build();
        assertThat(request.getStatements().get(0).getStatement(), is(queryMovieAndDirectorsColumns));
        assertThat(request.toString(), is(raw.toString()));
    }

}
