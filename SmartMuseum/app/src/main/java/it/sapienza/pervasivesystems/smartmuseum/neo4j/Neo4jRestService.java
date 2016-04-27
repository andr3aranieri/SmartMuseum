package it.sapienza.pervasivesystems.smartmuseum.neo4j;


import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * API interface for Accounts.
 */
public interface Neo4jRestService {

    @GET("/")
    Response getStatus();

    @POST("/db/data/transaction/commit")
    Response getCypherResponse(@Body CypherRequest request);

    @POST("/db/data/transaction/commit")
    Map<String,Object> getCypherMap(@Body CypherRequest request);

    @POST("/db/data/transaction/commit")
    <T> CypherResultSet<T> getCypherResultSet(@Body CypherRequest request);

    @POST("/db/data/transaction/commit")
    <T> void runCypherResultSet(@Body CypherRequest request, Callback<CypherResultSet<T>> callback);

}
