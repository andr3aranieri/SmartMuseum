package it.sapienza.pervasivesystems.smartmuseum.api;

import it.sapienza.pervasivesystems.smartmuseum.api.cyphers.QueryCypher;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by andrearanieri on 25/04/16.
 */
public interface UserAPI {
    @GET("/")
    Response getStatus();

    @POST("db/data/cypher")
    @Headers({
            "Accept: application/json; charset=UTF-8",
            "Authorization: Basic bmVvNGo6YW5kcmVh",
            "Content-Type: application/json"
    })
    Call<ResponseBody> searchUserByEmail(@Body QueryCypher query);

    @POST("db/data/transaction/commit")
    @Headers({
            "Accept: application/json; charset=UTF-8",
            "Authorization: Basic bmVvNGo6YW5kcmVh",
            "Content-Type: application/json"
    })
    Call<ResponseBody> searchUserByEmail2(@Body String statements);

    @POST("db/data/node")
    @Headers({
            "Accept: application/json; charset=UTF-8",
            "Authorization: Basic bmVvNGo6YW5kcmVh",
            "Content-Type: application/json"
    })
    Call<ResponseBody> createUser(@Body UserModel user);

}
