package com.openclassrooms.oc_p7.models.pojo_models.restaurant_pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantPojo {

    @SerializedName("address_components")
    public List<AddressComponent> addressComponents;
    @SerializedName("adr_address")
    public String adrAddress;
    @SerializedName("business_status")
    public String businessStatus;
    @SerializedName("formatted_address")
    public String formattedAddress;
    @SerializedName("formatted_phone_number")
    public String formattedPhoneNumber;
    public Geometry geometry;
    public String icon;
    @SerializedName("international_phone_number")
    public String internationalPhoneNumber;
    public String name;
    @SerializedName("opening_hours")
    public OpeningHours openingHours;
    public List<Photo> photos;
    @SerializedName("place_id")
    public String placeId;
    public PlusCode plus_code;
    @SerializedName("price_level")
    public int priceLevel;
    public double rating;
    public String reference;
    public List<Review> reviews;
    public String scope;
    public List<String> types;
    public String url;
    @SerializedName("user_ratings_total")
    public int userRatingsTotal;
    @SerializedName("utc_offset")
    public int utcOffset;
    public String vicinity;
    public String website;

}
