package com.openclassrooms.oc_p7.models.pojo_models.general;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Viewport implements Serializable {

    @SerializedName("northeast")
    public Northeast northeast;

    @SerializedName("southwest")
    public Southwest southwest;

}
