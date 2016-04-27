package it.sapienza.pervasivesystems.smartmuseum.model.db;

import it.sapienza.pervasivesystems.smartmuseum.api.APIConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andrearanieri on 25/04/16.
 */
public class DBManager {

    protected Retrofit retrofit;

    public DBManager() {
        this.retrofit = new Retrofit.Builder().baseUrl(APIConfig.API).addConverterFactory(GsonConverterFactory.create()).build();
    }
}
