package com.openclassrooms.oc_p7.models.pojo_models.responses;

import com.google.gson.annotations.SerializedName;
import com.openclassrooms.oc_p7.models.pojo_models.restaurant_pojo.RestaurantPojo;

import java.util.List;

public class NearbyPlaceResponse {

    public List<Object> html_attributions;

    public String next_page_token;

    @SerializedName("results")
    public List<RestaurantPojo> restaurantPojos;

    public String status;

}
