package com.openclassrooms.oc_p7.models.pojo_models.general;

import java.io.Serializable;
import java.util.List;

public class RestaurantPojo implements Serializable {

    public Geometry geometry;

    public String icon;

    public String name;

    public List<Photo> photos;

    public String place_id;

    public String reference;

    public String scope;

    public List<String> types;

    public String vicinity;

    public String business_status;

    public PlusCode plus_code;

    public double rating;

    public int user_ratings_total;

    public OpeningHours opening_hours;

    public int price_level;

}
