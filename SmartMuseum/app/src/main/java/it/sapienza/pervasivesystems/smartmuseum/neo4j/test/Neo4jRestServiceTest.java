package it.sapienza.pervasivesystems.smartmuseum.neo4j.test;

import android.util.Log;

import com.google.common.io.CharStreams;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherNodeSet;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRequest;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherResultSet;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRow;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherService;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherStatement;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.Neo4jRestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class Neo4jRestServiceTest {

    CypherService dao = new CypherService();
    Neo4jRestService neo4j = dao.getNeo4jRestService();

    String querySomeNodes = "MATCH (n) RETURN n LIMIT 10";
    String querySomeNodesWithLabels = "MATCH (n) WHERE length(labels(n)) > 0 RETURN n LIMIT 10";
    String queryMovieAndDirectorsObjects = "MATCH (m:Movie)-[r:DIRECTED]-(a:Person) WHERE m.title = \"The Matrix\" RETURN m,r,a";
    String queryMovieAndDirectorsColumns = "MATCH (m:Movie)-[r:DIRECTED]-(a:Person) WHERE m.title = \"The Matrix\" RETURN m.title,type(r),a.born";


    /**
     * Tests the raw REST response from Retrofit
     */
    @Test
    public void getCypherResponse() throws IOException {
        Log.i("getCypherResponse", "INIZIO");
        CypherStatement statement = CypherStatement.Builder().withCypher(queryMovieAndDirectorsObjects).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        Response response = neo4j.getCypherResponse(request);
        assertNotNull(response);
        assertThat(response.getStatus(), is(HttpStatus.SC_OK));
        String body = CharStreams.readLines(new InputStreamReader(response.getBody().in())).toString();
        assertTrue(body.length() > 0);
        System.out.println(body);
        Log.i("getCypherResponse", "body: " + body);
    }

    /**
     * Tests the generic Map response from Retrofit
     */
    @Test
    public void getCypherMap() throws IOException {
        CypherStatement statement = CypherStatement.Builder().withCypher(querySomeNodes).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        System.out.println(statement);
        System.out.println(request);
        Map<String, Object> response = neo4j.getCypherMap(request);
        assertNotNull(response);
        assertTrue(response.size() > 0);

        assertNotNull(response.get("errors"));
        assertThat(((List) response.get("errors")).size(), is(0));

        assertNotNull(response.get("results"));
        List<Map<String, Object>> listResults = (List<Map<String, Object>>) response.get("results");
        System.out.println(listResults);
        assertThat(listResults.size(), is(1));
        assertNotNull(listResults.get(0).get("columns"));
        assertNotNull(listResults.get(0).get("data"));
    }

    /**
     * Tests the NodeSet response from Retrofit
     */
    @Test
    public void getCypherResultSet() throws IOException {
        CypherStatement statement = CypherStatement.Builder().withCypher(querySomeNodes).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        CypherResultSet<List<Object>> result = neo4j.getCypherResultSet(request);
        CypherNodeSet<List<Object>> nodes = result.getResults().get(0);
        assertNotNull(nodes.getData());

        for(CypherRow cr: nodes.getData()) {
            System.out.println("rs="+cr.getRow());
        }
    }

    @Test
    public void getCypherNodesAndRelationshipsMap() throws IOException {
        CypherStatement statement = CypherStatement.Builder().withCypher(queryMovieAndDirectorsObjects).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        Map<String, Object> nodes = neo4j.getCypherMap(request);
        assert (nodes.size() > 0);
        assertNotNull(((Map) ((List) nodes.get("results")).get(0)).get("data"));
    }

    @Test
    public void getCypherNodesAndRelationshipsResponse() throws IOException {
        CypherStatement statement = CypherStatement.Builder().withCypher(queryMovieAndDirectorsObjects).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        Response nodes = neo4j.getCypherResponse(request);
        assertNotNull(nodes.getBody());
    }

    @Test
    public void getCypherNodesAndRelationshipsResultSet() throws IOException {
        CypherStatement statement = CypherStatement.Builder().withCypher(queryMovieAndDirectorsObjects).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        CypherResultSet<Object> result = neo4j.getCypherResultSet(request);
        CypherNodeSet<Object> nodes = result.getResults().get(0);
        assertNotNull(nodes.getData());
    }

    @Test
    public void getCypherNodesAndRelationshipsColumnsMap() throws IOException {
        CypherStatement statement = CypherStatement.Builder().withCypher(queryMovieAndDirectorsColumns).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        Map<String, Object> nodes = neo4j.getCypherMap(request);
        assert (nodes.size() > 0);
        assertNotNull(((Map) ((List) nodes.get("results")).get(0)).get("data"));
    }

    @Test
    public void getCypherNodesAndRelationshipsColumnsResponse() throws IOException {
        CypherStatement statement = CypherStatement.Builder().withCypher(queryMovieAndDirectorsColumns).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        Response response = neo4j.getCypherResponse(request);
        assertNotNull(response.getBody());
        Type resultType = new TypeToken<CypherResultSet<List<Object>>>() {
        }.getType();
        CypherResultSet<List<Object>> result = dao.getGson().fromJson(new InputStreamReader(response.getBody().in()), resultType);
        assertNotNull(result.getResults().get(0).getData().get(0).getRow().get(0));
    }

    @Test
    public void getCypherNodesAndRelationshipsColumnsResultSet() throws IOException {
        CypherStatement statement = CypherStatement.Builder().withCypher(queryMovieAndDirectorsColumns).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        CypherResultSet<List<Object>> result = neo4j.getCypherResultSet(request);
        assertNotNull(result.getResults().get(0).getData().get(0).getRow().get(0));
    }

    @Test
    public void getCypherNodesAndRelationshipsColumnsResultSetCallback() throws IOException, InterruptedException {
        CypherStatement statement = CypherStatement.Builder().withCypher(queryMovieAndDirectorsColumns).build();
        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
        neo4j.runCypherResultSet(request, new Callback<CypherResultSet<List<Object>>>() {

            @Override
            public void success(CypherResultSet<List<Object>> result, Response response) {
                CypherNodeSet<List<Object>> nodes = result.getResults().get(0);
                System.out.println(nodes);
                assertNotNull(result.getResults().get(0).getData().get(0).getRow().get(0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Assert.fail(retrofitError.getMessage());
            }
        });
        Thread.sleep(2000);
    }

}
