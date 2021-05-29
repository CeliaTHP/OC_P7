package com.openclassrooms.oc_p7.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbyPlaceResponse {

        @SerializedName("html_attributions")
        public List<Object> html_attributions;

        @SerializedName("next_page_token")
        public String next_page_token;

        @SerializedName("results")
        public List<Result> results;

        @SerializedName("status")
        public String status;

}
