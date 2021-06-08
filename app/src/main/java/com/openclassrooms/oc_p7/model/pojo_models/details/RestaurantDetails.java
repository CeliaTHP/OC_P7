package com.openclassrooms.oc_p7.model.pojo_models.details;

import com.openclassrooms.oc_p7.model.pojo_models.general.Geometry;
import com.openclassrooms.oc_p7.model.pojo_models.general.Photo;
import com.openclassrooms.oc_p7.model.pojo_models.general.PlusCode;

import java.util.List;

public class RestaurantDetails {

    public List<AddressComponent> address_components;
    public String adr_address;
    public String business_status;
    public String formatted_address;
    public String formatted_phone_number;
    public Geometry geometry;
    public String icon;
    public String international_phone_number;
    public String name;
    public OpeningHoursDetails opening_hours;
    public List<Photo> photos;
    public String place_id;
    public PlusCode plus_code;
    public int price_level;
    public double rating;
    public String reference;
    public List<Review> reviews;
    public List<String> types;
    public String url;
    public int user_ratings_total;
    public int utc_offset;
    public String vicinity;
    public String website;

}
