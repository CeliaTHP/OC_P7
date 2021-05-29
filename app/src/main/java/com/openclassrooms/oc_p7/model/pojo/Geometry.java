package com.openclassrooms.oc_p7.model.pojo;

import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("location")
    public Location location;
    @SerializedName("viewport")
    public Viewport viewport;

}
