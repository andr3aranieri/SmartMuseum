package it.sapienza.pervasivesystems.smartmuseum.neo4j.test;

import java.util.Map;

import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherService;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.Neo4jRestService;

public class Neo4jPojoTest {

    CypherService dao = new CypherService();
    Neo4jRestService neo4j = dao.getNeo4jRestService();

    String queryMovieAndDirectorsObjects = "MATCH (m:Movie)-[r:DIRECTED]-(a:Person) WHERE m.title = \"The Matrix\" RETURN m,r,a";
    String queryMovieAndDirectorsColumns = "MATCH (m:Movie)-[r:DIRECTED]-(a:Person) WHERE m.title = \"The Matrix\" RETURN m.title as title,type(r) as type,a.born as born";

    // ------------------------------------------------------
    // OBJECT based response
    // ------------------------------------------------------

    private static class MovieNode {
        public String title;
        public long released;
        public String tagline;

        @Override
        public String toString() {
            return "MovieNode{" +
                    "title='" + title + '\'' +
                    ", released=" + released +
                    ", tagline='" + tagline + '\'' +
                    '}';
        }
    }


    private static class PersonNode {
        public String name;
        public long born;

        @Override
        public String toString() {
            return "PersonNode{" +
                    "name='" + name + '\'' +
                    ", born=" + born +
                    '}';
        }
    }

    private static class MovieAndDirectorsObjectRow {
        public MovieNode movie;
        public Map<String, Object> rel;
        public PersonNode person;

        @Override
        public String toString() {
            return "MovieAndDirectorsObjectRow{" +
                    "movie=" + movie +
                    ", rel=" + rel +
                    ", person=" + person +
                    '}';
        }
    }


//    @Test
//    public void getCypherNodesAndRelationshipsObjectResultSet() throws IOException {
//        // build our cypher statement...
//        CypherStatement statement = CypherStatement.Builder().withCypher(queryMovieAndDirectorsObjects).build();
//        // build our neo4j request...
//        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
//        // execute our neo4j request and process the response...
//        CypherResultSet<MovieAndDirectorsObjectRow> result = dao.getNeo4jColumnResponse(
//                (JsonElement jElement, Type typeOfT, JsonDeserializationContext context) -> {
//                    System.out.println("Creating Object");
//                    MovieAndDirectorsObjectRow pojo = new MovieAndDirectorsObjectRow();
//                    int column = 0;
//                    pojo.movie = context.deserialize( jElement.getAsJsonObject().getAsJsonArray("row").get(column++), MovieNode.class);
//                    pojo.rel = context.deserialize( jElement.getAsJsonObject().getAsJsonArray("row").get(column++), Map.class);
//                    pojo.person = context.deserialize( jElement.getAsJsonObject().getAsJsonArray("row").get(column++), PersonNode.class);
//                    return pojo;
//                }).getCypherResultSet(request);
//        // validate the results are mapped into PoJo objects correctly...
//        assertNotNull(result.results.get(0).data.get(0).getRow().movie.title);
//    }


    // ------------------------------------------------------
    // COLUMN based response
    // ------------------------------------------------------

    /**
     * Simple PoJo to represent the columns returned from a cypher query of property values
     */
    private static class MovieAndDirectorsColumnRow {
        public String title;
        public String type;
        public long born;

        @Override
        public String toString() {
            return "MovieAndDirectorsRow{" +
                    "title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", born=" + born +
                    '}';
        }
    }

//    @Test
//    public void getCypherNodesAndRelationshipsColumnsResultSet() throws IOException {
//        // build our cypher statement...
//        CypherStatement statement = CypherStatement.Builder().withCypher(queryMovieAndDirectorsColumns).build();
//        // build our neo4j request...
//        CypherRequest request = CypherRequest.Builder().withStatement(statement).build();
//        // execute our neo4j request and process the response...
//        CypherResultSet<MovieAndDirectorsColumnRow> result = dao.getNeo4jColumnResponse(
//                (JsonElement jElement, Type typeOfT, JsonDeserializationContext context) -> {
//            System.out.println("Creating Object");
//            MovieAndDirectorsColumnRow pojo = new MovieAndDirectorsColumnRow();
//            int column = 0;
//            pojo.title = jElement.getAsJsonObject().getAsJsonArray("row").get(column++).getAsString();
//            pojo.type = jElement.getAsJsonObject().getAsJsonArray("row").get(column++).getAsString();
//            pojo.born = jElement.getAsJsonObject().getAsJsonArray("row").get(column++).getAsLong();
//            return pojo;
//        }).getCypherResultSet(request);
//        // validate the results are mapped into PoJo objects correctly...
//        assertNotNull(result.results.get(0).data.get(0).getRow().title);
//        assertNotNull(result.results.get(0).data.get(0).getRow().type);
//        assertTrue(result.results.get(0).data.get(0).getRow().born > 0);
//    }

}
