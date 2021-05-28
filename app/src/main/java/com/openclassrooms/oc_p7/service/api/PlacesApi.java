package com.openclassrooms.oc_p7.service.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApi {


    //TEST
    @GET("place/findplacefromtext/json?input=Museum%20of%20Contemporary%20Art%20Australia&inputtype=textquery&fields=formatted_address,name")
    Call<JsonObject> getSearch(
            @Query("key") String googleApiKey);
    //TEST

    @GET("place/nearbysearch/json")
    Call<JsonObject> getNearbyPlaces(
            @Query("location") String location,
            @Query("key") String googleApiKey,
            @Query("radius") String radius
    );


}
