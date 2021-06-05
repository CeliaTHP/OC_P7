package com.openclassrooms.oc_p7.model.pojo_models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photo {

    @SerializedName("height")
    public int height;

    @SerializedName("html_attributions")
    public List<String> html_attributions;

    @SerializedName("photo_reference")
    public String photo_reference;

    @SerializedName("width")
    public int width;
}