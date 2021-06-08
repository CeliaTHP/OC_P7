package com.openclassrooms.oc_p7.model.pojo_models.general;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbyPlaceResponse {

    public List<Object> html_attributions;

    public String next_page_token;

    @SerializedName("results")
    public List<Restaurant> restaurants;

    public String status;

}
