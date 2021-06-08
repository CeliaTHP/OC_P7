package com.openclassrooms.oc_p7.model.pojo_models.general;

import com.google.gson.annotations.SerializedName;

public class Viewport {

    @SerializedName("northeast")
    public Northeast northeast;

    @SerializedName("southwest")
    public Southwest southwest;

}
