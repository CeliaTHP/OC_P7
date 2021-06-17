package com.openclassrooms.oc_p7.services.apis;

import com.openclassrooms.oc_p7.models.pojo_models.details.DetailPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.general.NearbyPlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApi {

    @GET("place/nearbysearch/json")
    Call<NearbyPlaceResponse> getNearbyPlaces(
            @Query("key") String googleApiKey,
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("type") String type
    );

    @GET("place/details/json")
    Call<DetailPlaceResponse> getDetailsById(
            @Query("key") String googleApiKey,
            @Query("place_id") String placeId
    );


    //get details with id from workmate_table
    //save workmate


}
