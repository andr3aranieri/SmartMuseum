package it.sapienza.pervasivesystems.smartmuseum.model.db;


import android.util.Log;

import java.io.IOException;

import it.sapienza.pervasivesystems.smartmuseum.api.UserAPI;
import it.sapienza.pervasivesystems.smartmuseum.api.cyphers.QueryCypher;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by andrearanieri on 25/04/16.
 */
public class UserDB extends DBManager {
    private UserAPI userAPI;

    public UserModel getUserByEmail(String email){
        Log.i("ANDREA", "getUserByEmail()");
        Log.i("ANDREA", "api: " + this.retrofit.baseUrl().toString());
        final UserModel userModel = new UserModel();
        this.userAPI = this.retrofit.create(UserAPI.class);

        Call<ResponseBody> call = this.userAPI.searchUserByEmail(new QueryCypher("match (n) where n.email = '" + email + "' return n"));

        //the searchUserByEmail2 uses the correct db/data/transaction/commit for invoking a list of cyphers... try to make it work to use it instead the legacy db/data/cypher!
        //the problem should be the way a pass statements json: i should implement a pojo class that maps a list of statements;
//        Call<ResponseBody> call = this.userAPI.searchUserByEmail2("{\n" +
//                "  \"statements\" : [ {\n" +
//                "    \"statement\" : \"match (n) where n.email = 'and.ranieros@gmail.com' return n\"\n" +
//                "  } ]\n" +
//                "}");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> c, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    try {
                        Log.i("ANDREA", "*******************************User returned: " + response.body().string());
                        //set userModel values from response.body().string() json file, and pass the data asynchounosly to the UI;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //request not successful (like 400,401,403 etc)
                    //Handle errors
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> c, Throwable t) {
                Log.i("ANDREA", "Failure: " + t.getMessage());
            }
        });

        return userModel;
    }

    public boolean createUser(UserModel user) {
        boolean res = true;
        this.userAPI = this.retrofit.create(UserAPI.class);
        Call<ResponseBody> call = this.userAPI.createUser(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> c, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    try {
                        Log.i("ANDREA", "*******************************create user: " + response.body().string());
                        //set userModel values from response.body().string() json file, and pass the data asynchounosly to the UI;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //request not successful (like 400,401,403 etc)
                    //Handle errors
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> c, Throwable t) {
                Log.i("ANDREA", "create user failure: " + t.getMessage());
            }
        });
        return res;
    }
}
