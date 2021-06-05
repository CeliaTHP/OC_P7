package com.openclassrooms.oc_p7.model.pojo_models;

import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("location")
    public Location location;
    @SerializedName("viewport")
    public Viewport viewport;

}
