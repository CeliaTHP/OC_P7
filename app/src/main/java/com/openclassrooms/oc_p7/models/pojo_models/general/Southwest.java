package com.openclassrooms.oc_p7.models.pojo_models.general;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Southwest implements Serializable {

    @SerializedName("lat")
    public double lat;

    @SerializedName("lng")
    public double lng;

}
