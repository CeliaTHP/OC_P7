package com.openclassrooms.oc_p7.model.pojo_models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Restaurant {

    @SerializedName("geometry")
    public Geometry geometry;

    @SerializedName("icon")
    public String icon;

    @SerializedName("name")
    public String name;

    @SerializedName("photos")
    public List<Photo> photos;

    @SerializedName("place_id")
    public String place_id;

    @SerializedName("reference")
    public String reference;

    @SerializedName("scope")
    public String scope;

    @SerializedName("types")
    public List<String> types;

    @SerializedName("vicinity")
    public String vicinity;

    @SerializedName("business_status")
    public String business_status;

    @SerializedName("plus_code")
    public PlusCode plus_code;

    @SerializedName("rating")
    public double rating;

    @SerializedName("user_ratings_total")
    public int user_ratings_total;

    @SerializedName("opening_hours")
    public OpeningHours opening_hours;

    @SerializedName("price_level")
    public int price_level;


}
